package sk.bsmk.customer.bookkeeper

import java.util.UUID

import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.{EventEnvelope, Offset}
import akka.stream.Materializer
import akka.stream.javadsl.Sink
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.customer.CustomerActor.CustomerRegistered
import sk.bsmk.customer.bookkeeper.Bookkeeper.ProvideDetail
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.{CustomerActor, CustomerRepository, Email, RegistrationData}

import scala.concurrent.Future

object Bookkeeper {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)
  final case class ProvideDetail(email: Email)

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
      case EventEnvelope(_, persistenceId, _, CustomerRegistered(email)) ⇒
        logger.info("inserting")
        repository.insert(UUID.fromString(persistenceId), email)
      case e ⇒
        logger.error("Something unexpected {}", e)
        Future.successful("TODO")

    }
    .runWith(Sink.ignore)

  def provideDetail(query: ProvideDetail): Future[Option[CustomerDetail]] = {
    Thread.sleep(1000)
    val email = query.email
    logger.info("Retrieving data for {}", email)
    repository.find(email)
  }

}
