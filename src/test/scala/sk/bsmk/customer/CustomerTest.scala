package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.registrator.RegistratorActor

class CustomerTest extends WordSpec with Matchers {

  protected val system = ActorSystem("test")
  protected val registrator: ActorRef = system.actorOf(RegistratorActor.props)

  "Registrator" when {

    "receives new registration" should {

      "register new customer" in {

        registrator ! RegisterCustomer("some@email.com")

        Thread.sleep(1000)

      }
    }
  }

}
