package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.registrar.RegistrarActor

class CustomerTest extends WordSpec with Matchers {

  protected val system              = ActorSystem("test")
  protected val registrar: ActorRef = system.actorOf(RegistrarActor.props)

  "Registrar" when {

    "receives new registration" should {

      "register new customer" in {

        registrar ! RegisterCustomer("some@email.com")

        Thread.sleep(1000)

      }
    }
  }

}
