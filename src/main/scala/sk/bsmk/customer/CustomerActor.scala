package sk.bsmk.customer

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}
import sk.bsmk.customer.CustomerActor._
import sk.bsmk.customer.registrar.{RegistrationData, RegistrationResult, Stored}

import scala.concurrent.Promise

object CustomerActor {

  val Tag = "Customer"

  final case class Register(data: RegistrationData, promise: Promise[RegistrationResult])
  final case class AddVoucher(voucherId: String)

  case object GetState

  sealed trait CustomerEvent
  final case class CustomerRegistered(email: String) extends CustomerEvent
  final case class VoucherAdded(voucherId: String)   extends CustomerEvent

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

    case command @ Register(data, promise) ⇒
      log.info("Registering customer with id {} with {}", persistenceId, command)
      persist(CustomerRegistered(data.email)) { event ⇒
        updateState(event)
        promise.success(Stored)
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
