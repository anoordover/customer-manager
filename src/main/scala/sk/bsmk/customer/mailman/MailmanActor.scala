package sk.bsmk.customer.mailman

import akka.actor.{Actor, ActorLogging, Props}
import sk.bsmk.customer.Email
import sk.bsmk.customer.mailman.MailmanActor.{EmailAlreadyExists, RegistrationSuccessful}

object MailmanActor {

  final case class EmailAlreadyExists(email: Email)
  final case class RegistrationSuccessful(email: Email)

  val Registered  = "You have been registered"
  val EmailExists = "Your email is already used"

  def props(messagingService: MessagingService): Props = Props(new MailmanActor(messagingService))

}

class MailmanActor(
    messagingService: MessagingService
) extends Actor
    with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case EmailAlreadyExists(email) ⇒
      log.info("Sending email to {} about already existing email", email)
      messagingService.sendMessage(email, MailmanActor.EmailExists)
    case RegistrationSuccessful(email) ⇒
      log.info("Sending email to {} that customer was successfully registered", email)
      messagingService.sendMessage(email, MailmanActor.Registered)
  }

}
