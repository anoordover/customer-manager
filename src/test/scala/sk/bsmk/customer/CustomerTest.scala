package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.mailman.MailmanActor
import sk.bsmk.customer.registrar.RegistrarActor
import sk.bsmk.customer.registrar.RegistrarActor.RegisterCustomer

class CustomerTest extends WordSpec with Matchers {

  protected val system              = ActorSystem("test")
  protected val messagingService    = new MockMessagingService()
  protected val mailman: ActorRef   = system.actorOf(MailmanActor.props(messagingService))
  protected val registrar: ActorRef = system.actorOf(RegistrarActor.props(mailman))

  "Registrar" when {

    "receives new registration" should {

      "register new customer" in {

        registrar ! RegisterCustomer(RegistrationData("some@email.com"))

        Thread.sleep(1000)

      }
    }
  }

}
