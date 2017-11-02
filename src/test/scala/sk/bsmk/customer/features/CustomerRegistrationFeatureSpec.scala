package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerRegistrationRequest

import scala.concurrent.Future

class CustomerRegistrationFeatureSpec extends ApiFeatureSpec {

  "The customer registration endpoint" when {
    "accessed with POST request and valid body" should {

      implicit val futureResp: Future[HttpResponse] =
        Marshal(CustomerRegistrationRequest("some@email.com")).to[RequestEntity].flatMap { entity ⇒
          val request = HttpRequest(
            method = HttpMethods.POST,
            uri = s"$BaseUri/api/customers",
            entity = entity
          )
          Http().singleRequest(request)
        }

      haveStatusOk
//      shouldHaveContentType(ContentTypes.`application/json`)

    }
  }

}
