package sk.bsmk.customer.mailman

import akka.actor.{Actor, ActorLogging, Props}
import sk.bsmk.customer.Email
import sk.bsmk.customer.mailman.MailmanActor.{EmailAlreadyExists, RegistrationSuccessful}

object MailmanActor {

  final case class EmailAlreadyExists(email: Email)
  final case class RegistrationSuccessful(email: Email)

  def props: Props = Props[MailmanActor]

}

class MailmanActor extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case EmailAlreadyExists(email) ⇒
      log.info("Sending email to {} about already existing email", email)
    case RegistrationSuccessful(email) ⇒
      log.info("Sending email to {} that customer was successfully registered", email)
  }

}
