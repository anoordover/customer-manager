package sk.bsmk.customer.registrator

import akka.actor.{Actor, ActorLogging, Props}

object RegistratorActor {
  def props: Props = Props(new RegistratorActor(CustomerPersistenceUuidGenerator))
}

class RegistratorActor(persistenceIdGenerator: CustomerPersistenceIdGenerator) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case RegisterCustomer(email) ⇒
      val persistenceId = persistenceIdGenerator.generate()
      log.info("Generated {} for registration with {}", persistenceId, email)

  }

}
