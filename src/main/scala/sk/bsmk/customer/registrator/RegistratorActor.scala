package sk.bsmk.customer.registrator

import akka.actor.{Actor, ActorLogging, Props}
import sk.bsmk.customer.{CustomerActor, RegisterCustomer}

object RegistratorActor {
  def props: Props = Props(new RegistratorActor(CustomerPersistenceUuidGenerator))
}

class RegistratorActor(persistenceIdGenerator: CustomerPersistenceIdGenerator) extends Actor with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case command: RegisterCustomer ⇒
      val persistenceId = persistenceIdGenerator.generate()
      log.info("Generated '{}' for {}", persistenceId, command)
      val newCustomer = context.actorOf(CustomerActor.props(persistenceId))

      newCustomer ! command
  }

}
