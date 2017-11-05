package sk.bsmk.customer.bookkeeper

import java.util.UUID

import akka.actor.ActorRef
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, Offset}
import akka.stream.Materializer
import akka.stream.javadsl.Sink
import org.slf4j.LoggerFactory
import sk.bsmk.customer.CustomerActor.CustomerRegistered
import sk.bsmk.customer.bookkeeper.Bookkeeper.{CheckMailAndRegisterOrDecline, ProvideDetail}
import sk.bsmk.customer.mailman.MailmanActor.{CustomerDetailResponse, EmailAlreadyExists, NoInformationFound}
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer
import sk.bsmk.customer.{CustomerActor, CustomerRepository, Email, RegistrationData}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future
import scala.util.{Failure, Success}

object Bookkeeper {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)
  final case class ProvideDetail(email: Email)

  def apply(repository: CustomerRepository, registrar: ActorRef, mailman: ActorRef, readJournal: JdbcReadJournal)(
      implicit materializer: Materializer
  ) = new Bookkeeper(repository, registrar, mailman, readJournal)

}

class Bookkeeper(
    repository: CustomerRepository,
    registrar: ActorRef,
    mailman: ActorRef,
    readJournal: JdbcReadJournal
)(
    implicit materializer: Materializer
) {

  private val log = LoggerFactory.getLogger(this.getClass)

  readJournal
    .eventsByTag(CustomerActor.Tag, Offset.noOffset)
    .mapAsync(1) {
      case EventEnvelope(_, persistenceId, _, CustomerRegistered(email)) ⇒
        log.info("inserting")
        repository.insert(UUID.fromString(persistenceId), email)
      case e ⇒
        log.error("Something unexpected {}", e)
        Future.successful("TODO")

    }
    .runWith(Sink.ignore)

  def receive(command: CheckMailAndRegisterOrDecline): Unit = {
    val data = command.data
    log.info("Checking existence of {}", data.email)
    repository.find(data.email).onComplete {
      case Failure(e) ⇒ log.error("Problem when checking customer existence", e)
      case Success(None) ⇒
        log.debug("No existing record found for {}", data.email)
        registrar ! RegisterCustomer(data)
      case Success(Some(_)) ⇒
        log.debug("Existing record found for {}", data.email)
        mailman ! EmailAlreadyExists(data.email)
    }
  }

  def receive(command: ProvideDetail): Unit = {
    val email = command.email
    log.info("Retrieving data for {}")
    repository.find(email).onComplete {
      case Failure(e) ⇒ log.error("Problem when checking customer existence", e)
      case Success(None) ⇒
        log.debug("No existing record found for {}", email)
        mailman ! NoInformationFound(email)
      case Success(Some(detail)) ⇒
        log.debug("Existing record found for {}", email)
        mailman ! CustomerDetailResponse(detail.email, detail.points)
    }
  }

}
