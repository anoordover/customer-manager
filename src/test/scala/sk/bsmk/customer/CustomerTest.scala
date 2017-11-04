package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.bookkeeper.BookkeeperActor
import sk.bsmk.customer.mailman.MailmanActor
import sk.bsmk.customer.registrar.RegistrarActor
import sk.bsmk.customer.representative.Representative
import sk.bsmk.customer.representative.Representative.RegistrationRequest

import scala.concurrent.Await
import scala.concurrent.duration._

class CustomerTest extends WordSpec with Matchers with DbSupport {

  protected val system = ActorSystem("test")

  protected val messagingService = new MockMessagingService()

  protected val mailman: ActorRef        = system.actorOf(MailmanActor.props(messagingService))
  protected val registrar: ActorRef      = system.actorOf(RegistrarActor.props(mailman))
  protected val bookkeeper: ActorRef     = system.actorOf(BookkeeperActor.props(repository, registrar, mailman))
  protected val representative: ActorRef = system.actorOf(Representative.props(bookkeeper))

  "Registrar" when {

    "receives new registration" should {

      "register new customer" in {

        val data = RegistrationData("some@email.com")

        representative ! RegistrationRequest(data)

        val sentData = Await.result(messagingService.data(), 10.seconds)

        sentData.endpoint shouldEqual "some@email.com"
        sentData.message shouldEqual MailmanActor.Registered

        messagingService.reset()

      }
    }
  }

}
