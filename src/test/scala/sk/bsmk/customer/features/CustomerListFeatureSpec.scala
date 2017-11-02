package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, HttpResponse}
import org.scalatest.DoNotDiscover
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerListResponse

import scala.concurrent.Future

@DoNotDiscover
class CustomerListFeatureSpec extends ApiFeatureSpec {

  "The customer list endpoint" when {
    "accessed with GET request" should {
      implicit val futureResp: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api/customers"))

      haveStatusOk
      haveContentType(ContentTypes.`application/json`)
      haveEntity[CustomerListResponse]("return two items") { entity ⇒
        entity.items should have size 2
      }
    }
  }

}
