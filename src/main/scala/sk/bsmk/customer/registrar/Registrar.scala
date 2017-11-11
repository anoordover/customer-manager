package sk.bsmk.customer.registrar

import akka.actor.ActorSystem
import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.customer.CustomerActor.Register
import sk.bsmk.customer.bookkeeper.Bookkeeper
import sk.bsmk.customer.registrar.Registrar.RegisterCustomer
import sk.bsmk.customer.CustomerActor
import sk.bsmk.customer.detail.CustomerPersistenceId

import scala.concurrent.{Future, Promise}

object Registrar {

  final case class RegisterCustomer(data: RegistrationData)

  def apply(bookkeeper: Bookkeeper)(implicit actorSystem: ActorSystem): Registrar = new Registrar(
    bookkeeper
  )

}

class Registrar(
    bookkeeper: Bookkeeper
)(implicit actorSystem: ActorSystem)
    extends LazyLogging {

  def register(registerCustomer: RegisterCustomer): Future[RegistrationResult] = registerCustomer match {
    case RegisterCustomer(data) ⇒
      logger.info("Registering {}", data)

      val persistenceId = CustomerPersistenceId.fromUsername(data.username)
      logger.debug("Generated '{}' for {}", persistenceId, data)
      val newCustomer = actorSystem.actorOf(CustomerActor.props(persistenceId))

      val promise = Promise[RegistrationResult]
      newCustomer ! Register(data, promise)
      promise.future
  }

}
