package sk.bsmk.customer.mailman

import akka.actor.{Actor, ActorLogging, Props}

object MailmanActor {

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
