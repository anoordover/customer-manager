package sk.bsmk.customer.bookkeeper

import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, Offset}
import akka.stream.Materializer
import akka.stream.javadsl.Sink
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.customer.CustomerActor.CustomerRegistered
import sk.bsmk.customer.bookkeeper.Bookkeeper.ProvideDetail
import sk.bsmk.customer.detail.{CustomerDetail, CustomerPersistenceId}
import sk.bsmk.customer.registrar.RegistrationData
import sk.bsmk.customer.{CustomerActor, CustomerRepository, Email, Username}

import scala.concurrent.Future

object Bookkeeper {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)
  final case class ProvideDetail(username: Username)

  def apply(repository: CustomerRepository, readJournal: JdbcReadJournal)(
      implicit materializer: Materializer
  ) = new Bookkeeper(repository, readJournal)

}

class Bookkeeper(
    repository: CustomerRepository,
    readJournal: JdbcReadJournal
)(
    implicit materializer: Materializer
) extends LazyLogging {

  readJournal
    .eventsByTag(CustomerActor.Tag, Offset.noOffset)
    .mapAsync(1) {
      case EventEnvelope(_, _, _, CustomerRegistered(data)) ⇒
        logger.info("inserting")
        repository.insert(data)
      case e ⇒
        logger.error("Something unexpected {}", e)
        Future.successful("TODO")

    }
    .runWith(Sink.ignore)

  def provideDetail(query: ProvideDetail): Future[Option[CustomerDetail]] = {
    val username = query.username
    logger.info("Retrieving data for {}", username)
    repository.find(username)
  }

}
