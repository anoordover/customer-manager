package sk.bsmk.customer.bookkeeper

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.RegistrationData
import sk.bsmk.customer.bookkeeper.BookkeeperActor.CheckMailAndRegisterOrDecline
import sk.bsmk.customer.mailman.MailmanActor.EmailAlreadyExists
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer
import sk.bsmk.customer.repository.CustomerRepository

import scala.util.{Failure, Success}

import scala.concurrent.ExecutionContext.Implicits.global

object BookkeeperActor {

  final case class CheckMailAndRegisterOrDecline(data: RegistrationData)

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
      val future = repository.find(data.email)
      future.onComplete {
        case Failure(e)       ⇒ log.error("Problem when checking customer existence", e)
        case Success(None)    ⇒ registrar ! RegisterCustomer(data)
        case Success(Some(_)) ⇒ mailman ! EmailAlreadyExists(data.email)
      }
  }

}
