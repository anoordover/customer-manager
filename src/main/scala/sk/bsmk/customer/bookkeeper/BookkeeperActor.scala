package sk.bsmk.customer.bookkeeper

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.{Email, RegistrationData}
import sk.bsmk.customer.bookkeeper.BookkeeperActor.{CheckMailAndRegisterOrDecline, ProvideDetail}
import sk.bsmk.customer.mailman.MailmanActor.{CustomerDetailResponse, EmailAlreadyExists, NoInformationFound}
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer
import sk.bsmk.customer.repository.CustomerRepository

import scala.util.{Failure, Success}
import scala.concurrent.ExecutionContext.Implicits.global

object BookkeeperActor {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)
  final case class ProvideDetail(email: Email)

  def props(repository: CustomerRepository, registrar: ActorRef, mailman: ActorRef) =
    Props(new BookkeeperActor(repository, registrar, mailman))

}

class BookkeeperActor(
    repository: CustomerRepository,
    registrar: ActorRef,
    mailman: ActorRef
) extends Actor
    with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case CheckMailAndRegisterOrDecline(data) ⇒
      log.info("Checking existence of {}", data.email)
      repository.find(data.email).onComplete {
        case Failure(e) ⇒ log.error("Problem when checking customer existence", e)
        case Success(None) ⇒
          log.debug("No existing record found for {}", data.email)
          registrar ! RegisterCustomer(data)
        case Success(Some(_)) ⇒
          log.debug("Existing record found for {}", data.email)
          mailman ! EmailAlreadyExists(data.email)
      }

    case ProvideDetail(email) ⇒
      log.info("Retrieving data for {}")
      repository.find(email).onComplete {
        case Failure(e) ⇒ log.error("Problem when checking customer existence", e)
        case Success(None) ⇒
          log.debug("No existing record found for {}", email)
          mailman ! NoInformationFound(email)
        case Success(Some(detail)) ⇒
          log.debug("Existing record found for {}", email)
          mailman ! CustomerDetailResponse(detail.email, detail.points)
      }
  }

}
