package sk.bsmk.customer.bookkeeper

import akka.actor.{Actor, ActorLogging, Props}
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import sk.bsmk.customer.repository.CustomerRepository

object BookkeeperActor {

  def props(customerRepository: CustomerRepository) = Props(new BookkeeperActor(customerRepository))

}

class BookkeeperActor(
    customerRepository: CustomerRepository
) extends Actor
    with ActorLogging {

  val readJournal: JdbcReadJournal =
    PersistenceQuery(context.system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  override def receive = {
    ???
  }

}
