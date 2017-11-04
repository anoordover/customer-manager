package sk.bsmk.customer.wordspecs

import akka.http.scaladsl.Http
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import org.scalatest.DoNotDiscover
import sk.bsmk.customer.api.CustomerRegistrationRequest

import scala.concurrent.Future

@DoNotDiscover
class CustomerRegistrationWordSpec extends ApiWordSpec {

  "The customer registration endpoint" when {
    "accessed with POST request and valid body" should {

      lazy val futureResponse: Future[HttpResponse] =
        Marshal(CustomerRegistrationRequest("some@email.com")).to[RequestEntity].flatMap { entity ⇒
          val request = HttpRequest(
            method = HttpMethods.POST,
            uri = s"$BaseUri/api/customers",
            entity = entity
          )
          Http().singleRequest(request)
        }

      haveStatusOk(futureResponse)

      //      shouldHaveContentType(ContentTypes.`application/json`)

    }
  }

}
