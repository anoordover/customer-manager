package sk.bsmk.customer.registrar

import akka.actor.{Actor, ActorLogging, ActorRef, Props}
import sk.bsmk.customer.CustomerActor.Register
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer
import sk.bsmk.customer.{CustomerActor, RegistrationData}

object RegistrarActor {

  final case class RegisterCustomer(data: RegistrationData)

  def props(mailman: ActorRef): Props = Props(new RegistrarActor(CustomerPersistenceUuidGenerator, mailman))

}

class RegistrarActor(
    persistenceIdGenerator: CustomerPersistenceIdGenerator,
    mailman: ActorRef
) extends Actor
    with ActorLogging {

  override def receive: PartialFunction[Any, Unit] = {
    case RegisterCustomer(data) ⇒
      log.info("Registering {}", data)

      val persistenceId = persistenceIdGenerator.generate()
      log.debug("Generated '{}' for {}", persistenceId, data)
      val newCustomer = context.actorOf(CustomerActor.props(persistenceId))

      newCustomer ! Register(data, mailman)

  }

}
