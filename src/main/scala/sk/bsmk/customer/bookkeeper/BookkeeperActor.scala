package sk.bsmk.customer.bookkeeper

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.RegistrationData
import sk.bsmk.customer.bookkeeper.BookkeeperActor.CheckMailAndRegisterOrDecline
import sk.bsmk.customer.repository.CustomerRepository

object BookkeeperActor {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)

  def props(repository: CustomerRepository, registrar: ActorRef) = Props(new BookkeeperActor(repository, registrar))

}

class BookkeeperActor(
    repository: CustomerRepository,
    registrar: ActorRef
) extends Actor
    with ActorLogging {

  override def receive = {
    case CheckMailAndRegisterOrDecline(data) ⇒
      ???
  }

}
