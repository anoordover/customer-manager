package sk.bsmk.customer

import akka.actor.{ActorLogging, ActorRef, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import sk.bsmk.customer.CustomerActor.{AddVoucher, GetState, Register}
import sk.bsmk.customer.mailman.MailmanActor.RegistrationSuccessful

object CustomerActor {

  final case class Register(data: RegistrationData, mailman: ActorRef)
  final case class AddVoucher(voucherId: String)

  case object GetState

  def props(persistenceId: String): Props = Props(new CustomerActor(persistenceId))

}

class CustomerActor(val persistenceId: String) extends PersistentActor with ActorLogging {

  val snapShotInterval = 10

  var state: Option[Customer] = Option.empty

  private def updateState(event: CustomerEvent): Unit = event match {

    case CustomerRegistered(email) ⇒
      state = Option(Customer(email))

    case VoucherAdded(voucherId) ⇒
      state = state.map(_.addVoucher(voucherId))

  }

  override def receiveRecover: PartialFunction[Any, Unit] = {
    case event: CustomerRegistered            ⇒ updateState(event)
    case event: VoucherAdded                  ⇒ updateState(event)
    case SnapshotOffer(_, snapshot: Customer) ⇒ state = Option(snapshot)
  }

  override def receiveCommand: PartialFunction[Any, Unit] = {

    case command @ Register(data, mailman) ⇒
      log.info("Registering customer with id {} with {}", persistenceId, command)
      persist(CustomerRegistered(data.email)) { event ⇒
        updateState(event)
        mailman ! RegistrationSuccessful(data.email)
      }

    case command @ AddVoucher(voucherId) ⇒
      log.info("Applying {} to customer with {}", command, persistenceId)
      persist(VoucherAdded(voucherId)) { event ⇒
        updateState(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(state)
      }

    case GetState ⇒ sender() ! state
  }

}

sealed trait CustomerEvent
final case class CustomerRegistered(email: String) extends CustomerEvent
final case class VoucherAdded(voucherId: String)   extends CustomerEvent
