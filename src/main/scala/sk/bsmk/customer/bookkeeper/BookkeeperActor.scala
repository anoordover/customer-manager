package sk.bsmk.customer.bookkeeper

import java.util.UUID

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, Offset}
import akka.stream.Materializer
import akka.stream.javadsl.Sink
import sk.bsmk.customer.CustomerActor.CustomerRegistered
import sk.bsmk.customer.bookkeeper.BookkeeperActor.{CheckMailAndRegisterOrDecline, ProvideDetail}
import sk.bsmk.customer.mailman.MailmanActor.{CustomerDetailResponse, EmailAlreadyExists, NoInformationFound}
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer
import sk.bsmk.customer.repository.CustomerRepository
import sk.bsmk.customer.{CustomerActor, Email, RegistrationData}

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object BookkeeperActor {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)
  final case class ProvideDetail(email: Email)

  def props(repository: CustomerRepository, registrar: ActorRef, mailman: ActorRef, readJournal: JdbcReadJournal)(
      implicit materializer: Materializer
  ) =
    Props(new BookkeeperActor(repository, registrar, mailman, readJournal))

}

class BookkeeperActor(
    repository: CustomerRepository,
    registrar: ActorRef,
    mailman: ActorRef,
    readJournal: JdbcReadJournal
)(
    implicit materializer: Materializer
) extends Actor
    with ActorLogging {

  override def preStart(): Unit = {
    readJournal
      .eventsByTag(CustomerActor.Tag, Offset.noOffset)
      .mapAsync(1) {
        case EventEnvelope(_, persistenceId, sequenceNr, CustomerRegistered(email)) ⇒
          log.info("inserting")
          repository.insert(UUID.fromString(persistenceId), email)
        case e ⇒
          log.error("Something unexpected {}", e)
          Future.successful("TODO")

      }
      .runWith(Sink.ignore)
  }

  override def receive: PartialFunction[Any, Unit] = {
    case CheckMailAndRegisterOrDecline(data) ⇒
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

    case ProvideDetail(email) ⇒
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
