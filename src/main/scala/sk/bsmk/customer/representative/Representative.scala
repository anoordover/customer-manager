package sk.bsmk.customer.representative

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.{Email, RegistrationData}
import sk.bsmk.customer.bookkeeper.BookkeeperActor.{CheckMailAndRegisterOrDecline, ProvideDetail}
import sk.bsmk.customer.representative.Representative.{DetailRequest, RegistrationRequest}

object Representative {

  final case class RegistrationRequest(data: RegistrationData)
  final case class DetailRequest(email: Email)

  def props(bookkeeper: ActorRef): Props = Props(new Representative(bookkeeper))

}

class Representative(bookkeeper: ActorRef) extends Actor with ActorLogging {
  override def receive: PartialFunction[Any, Unit] = {
    case RegistrationRequest(data) ⇒
      log.info("Processing registration request {}", data)
      bookkeeper ! CheckMailAndRegisterOrDecline(data)

    case DetailRequest(email) ⇒
      log.info("Processing detail request for {}", email)
      bookkeeper ! ProvideDetail(email)

  }
}
