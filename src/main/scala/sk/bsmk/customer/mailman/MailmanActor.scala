package sk.bsmk.customer.mailman

import akka.actor.{Actor, ActorLogging, Props}
import sk.bsmk.customer.Email
import sk.bsmk.customer.mailman.MailmanActor.{
  CustomerDetailResponse,
  EmailAlreadyExists,
  NoInformationFound,
  RegistrationSuccessful
}

object MailmanActor {

  final case class EmailAlreadyExists(email: Email)
  final case class RegistrationSuccessful(email: Email)

  final case class NoInformationFound(email: Email)
  final case class CustomerDetailResponse(email: Email, points: Int)

  val Registered  = "You have been registered"
  val EmailExists = "Your email is already used"

  val NoDetailCanBeProvided = "No detail can be provided"

  def props(messagingService: MessagingService): Props = Props(new MailmanActor(messagingService))

}

class MailmanActor(
    messagingService: MessagingService
) extends Actor
    with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case EmailAlreadyExists(email) ⇒
      log.debug("Sending email to {} about already existing email", email)
      messagingService.sendMessage(MessagingServiceData(email, MailmanActor.EmailExists))
    case RegistrationSuccessful(email) ⇒
      log.debug("Sending email to {} that customer was successfully registered", email)
      messagingService.sendMessage(MessagingServiceData(email, MailmanActor.Registered))
    case NoInformationFound(email) ⇒
      log.debug("Sending email to {} that no information was found", email)
      messagingService.sendMessage(MessagingServiceData(email, MailmanActor.NoDetailCanBeProvided))
    case CustomerDetailResponse(email, points) ⇒
      log.info("Sending email to {} about detail information", email)
      messagingService.sendMessage(MessagingServiceData(email, s"Points: $points"))
  }

}
