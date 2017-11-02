package sk.bsmk.customer.registration

import akka.actor.{Actor, ActorLogging, Props}

object CustomerRegistrator {
  def props: Props = Props(new CustomerRegistrator(CustomerPersistenceUuidGenerator))
}

class CustomerRegistrator(persistenceIdGenerator: CustomerPersistenceIdGenerator) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case RegisterCustomer(email) ⇒
      val persistenceId = persistenceIdGenerator.generate()
      log.info("Generated {} for registration with {}", persistenceId, email)

  }

}
