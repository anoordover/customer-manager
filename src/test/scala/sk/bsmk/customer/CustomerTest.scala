package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.persistence.jdbc.query.scaladsl.JdbcReadJournal
import akka.persistence.query.PersistenceQuery
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, BeforeAndAfter, Matchers}
import sk.bsmk.customer.bookkeeper.Bookkeeper
import sk.bsmk.customer.persistence.model.Tables
import sk.bsmk.customer.registrar.Registrar
import sk.bsmk.customer.representative.Representative
import sk.bsmk.customer.representative.Representative.RegistrationRequest

class CustomerTest extends AsyncWordSpec with Matchers with DbSupport with BeforeAndAfter {

  implicit val system: ActorSystem             = ActorSystem("test")
  implicit val materializer: ActorMaterializer = ActorMaterializer()
  protected val readJournal: JdbcReadJournal =
    PersistenceQuery(system).readJournalFor[JdbcReadJournal](JdbcReadJournal.Identifier)

  protected val bookkeeper: Bookkeeper = Bookkeeper(customerRepository, readJournal)
  protected val registrar: Registrar   = Registrar(bookkeeper)

  protected val representative: Representative = Representative(registrar, bookkeeper)

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

        representative
          .processRegistration(RegistrationRequest(data))
          .map { detailOption ⇒
            detailOption should not be empty
          //            sentData.message shouldEqual Mailman.Registered
          }
      }
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
//  }

}
