package sk.bsmk.customer.representative

import akka.actor.{Actor, ActorLogging, Props}
import sk.bsmk.customer.bookkeeper.Bookkeeper
import sk.bsmk.customer.bookkeeper.Bookkeeper.{CheckMailAndRegisterOrDecline, ProvideDetail}
import sk.bsmk.customer.representative.Representative.{DetailRequest, RegistrationRequest}
import sk.bsmk.customer.{Email, RegistrationData}

object Representative {

  final case class RegistrationRequest(data: RegistrationData)
  final case class DetailRequest(email: Email)

  def props(bookkeeper: Bookkeeper): Props = Props(new Representative(bookkeeper))

}

class Representative(bookkeeper: Bookkeeper) extends Actor with ActorLogging {
  override def receive: PartialFunction[Any, Unit] = {
    case RegistrationRequest(data) ⇒
      log.info("Processing registration request {}", data)
      bookkeeper.receive(CheckMailAndRegisterOrDecline(data))

    case DetailRequest(email) ⇒
      log.info("Processing detail request for {}", email)
      bookkeeper.receive(ProvideDetail(email))

  }
}
