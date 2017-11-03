package sk.bsmk.customer.wordspec

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, HttpResponse}
import org.scalatest.DoNotDiscover
import sk.bsmk.customer.api.CustomerListResponse

import scala.concurrent.Future

@DoNotDiscover
class CustomerListWordSpec extends ApiWordSpec {

  "The customer list endpoint" when {
    "accessed with GET request" should {

      lazy val futureResp: Future[HttpResponse] =
        Http().singleRequest(HttpRequest(uri = s"$BaseUri/api/customers"))

      haveStatusOk(futureResp)
      haveContentType(ContentTypes.`application/json`, futureResp)
      haveEntity[CustomerListResponse]("return two items", futureResp) { entity ⇒
        entity.items should have size 2
      }

    }
  }

}
