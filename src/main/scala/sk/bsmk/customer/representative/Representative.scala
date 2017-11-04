package sk.bsmk.customer.representative

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.RegistrationData
import sk.bsmk.customer.bookkeeper.BookkeeperActor.CheckMailAndRegisterOrDecline
import sk.bsmk.customer.representative.Representative.RegistrationRequest

object Representative {

  final case class RegistrationRequest(data: RegistrationData)

  def props(bookkeeper: ActorRef): Props = Props(new Representative(bookkeeper))

}

class Representative(bookkeeper: ActorRef) extends Actor with ActorLogging {
  override def receive = {
    case RegistrationRequest(data) ⇒
      log.info("Processing registration request {}", data)
      bookkeeper ! CheckMailAndRegisterOrDecline(data)
  }
}
