package sk.bsmk.manager.customer

import akka.actor.{ActorLogging, Props}
import akka.persistence.{PersistentActor, SnapshotOffer}

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
    case event: VoucherAdded ⇒ updateState(event)
    case SnapshotOffer(_, snapshot: Customer) => state = Option(snapshot)
  }

  override def receiveCommand: PartialFunction[Any, Unit] = {

    case command@RegisterCustomer(email) ⇒
      log.info("Registering customer with id {} with {}", persistenceId, command)
      persist(CustomerRegistered(email)) { event ⇒
        updateState(event)
        context.system.eventStream.publish(event)
      }

    case command@AddVoucher(voucherId) ⇒
      log.info("Applying {} to customer with {}", command, persistenceId)
      persist(VoucherAdded(voucherId)) { event ⇒
        updateState(event)
        context.system.eventStream.publish(event)
        if (lastSequenceNr % snapShotInterval == 0 && lastSequenceNr != 0)
          saveSnapshot(state)
      }

    case GetState ⇒ sender() ! state
  }

}

object CustomerActor {

  def props(persistenceId: String): Props = Props(new CustomerActor(persistenceId))

}

