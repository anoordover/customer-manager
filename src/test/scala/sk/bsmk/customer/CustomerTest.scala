package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.bookkeeper.BookkeeperActor
import sk.bsmk.customer.mailman.MailmanActor
import sk.bsmk.customer.registrar.RegistrarActor
import sk.bsmk.customer.representative.Representative
import sk.bsmk.customer.representative.Representative.{DetailRequest, RegistrationRequest}

import scala.concurrent.Await
import scala.concurrent.duration._

class CustomerTest extends WordSpec with Matchers with DbSupport {

  implicit val system: ActorSystem             = ActorSystem("test")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  protected val readJournal: JdbcReadJournal =
    PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  protected val messagingService = new MockMessagingService()

  protected val mailman: ActorRef   = system.actorOf(MailmanActor.props(messagingService))
  protected val registrar: ActorRef = system.actorOf(RegistrarActor.props(mailman))
  protected val bookkeeper: ActorRef =
    system.actorOf(BookkeeperActor.props(repository, registrar, mailman, readJournal))
  protected val representative: ActorRef = system.actorOf(Representative.props(bookkeeper))

  "Representative" when {

    "receives registration request" should {

      "register new customer" in {

        val data = RegistrationData("some@email.com")

        representative ! RegistrationRequest(data)

        val sentData = Await.result(messagingService.data(), 10.seconds)

        sentData.endpoint shouldEqual "some@email.com"
        sentData.message shouldEqual MailmanActor.Registered

        messagingService.reset()

      }
    }

    "receives detail request" should {

      "provide customer derail" in {

        val email = "some1@email.com"

        val data = RegistrationData(email)

        representative ! RegistrationRequest(data)

        val sentData = Await.result(messagingService.data(), 10.seconds)

        sentData.endpoint shouldEqual email
        sentData.message shouldEqual MailmanActor.Registered

        messagingService.reset()

        Thread.sleep(1000)

        representative ! DetailRequest(email)

        val detailData = Await.result(messagingService.data(), 10.seconds)

        detailData.endpoint shouldEqual email
        detailData.message shouldEqual "Points: 0"

        messagingService.reset()

      }
    }

  }

}
