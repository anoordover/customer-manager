package sk.bsmk.customer.registration

import akka.actor.ActorLogging
import akka.persistence.PersistentActor

class CustomerRegistrationActor(val persistenceId: String) extends PersistentActor with ActorLogging {

  override def receiveRecover: Nothing = {
    throw CustomerExistsException(persistenceId)
  }

  override def receiveCommand: Nothing = {
    ???
//    case RegisterCustomer(email) ⇒ {
//      persist()
//    }
  }

}
