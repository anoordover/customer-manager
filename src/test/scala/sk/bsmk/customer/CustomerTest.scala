package sk.bsmk.customer

import akka.actor.{ActorRef, ActorSystem}
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import org.scalatest.{BeforeAndAfter, Ignore, Matchers, WordSpec}
import sk.bsmk.customer.bookkeeper.Bookkeeper
import sk.bsmk.customer.mailman.MailmanActor
import sk.bsmk.customer.persistence.model.Tables
import sk.bsmk.customer.registrar.RegistrarActor
import sk.bsmk.customer.representative.Representative
import sk.bsmk.customer.representative.Representative.{DetailRequest, RegistrationRequest}

import scala.concurrent.Await
import scala.concurrent.duration._

class CustomerTest extends WordSpec with Matchers with DbSupport with BeforeAndAfter {

  implicit val system: ActorSystem             = ActorSystem("test")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  protected val readJournal: JdbcReadJournal =
    PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)
  protected val messagingService         = new MockMessagingService()
  protected val mailman: ActorRef        = system.actorOf(MailmanActor.props(messagingService))
  protected val registrar: ActorRef      = system.actorOf(RegistrarActor.props(mailman))
  protected val bookkeeper: Bookkeeper   = Bookkeeper(customerRepository, registrar, mailman, readJournal)
  protected val representative: ActorRef = system.actorOf(Representative.props(bookkeeper))

  before {
    dsl.execute("DELETE FROM PUBLIC.\"snapshot\"")
    dsl.execute("DELETE FROM PUBLIC.\"journal\"")
    dsl.deleteFrom(Tables.CUSTOMERS).execute()
  }

  "Representative" when {

    "receives registration request" should {

      "register new customer" in {

        val email = "some@email.com"

        val data = RegistrationData(email)

        representative ! RegistrationRequest(data)

        val sentData = Await.result(messagingService.data(), 10.seconds)

        sentData.endpoint shouldEqual email
        sentData.message shouldEqual MailmanActor.Registered

        messagingService.reset()

      }
    }

//    "receives detail request" should {
//
//      "provide customer derail" in {
//
//        val email = "another@email.com"
//
//        val data = RegistrationData(email)
//
//        representative ! RegistrationRequest(data)
//
//        val sentData = Await.result(messagingService.data(), 10.seconds)
//
//        sentData.endpoint shouldEqual email
//        sentData.message shouldEqual MailmanActor.Registered
//
//        messagingService.reset()
//
//        representative ! DetailRequest(email)
//
//        val detailData = Await.result(messagingService.data(), 10.seconds)
//
//        detailData.endpoint shouldEqual email
//        detailData.message shouldEqual "Points: 0"
//
//        messagingService.reset()
//
//      }
//    }
  }

}
