package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, HttpResponse}
import org.scalatest.DoNotDiscover
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerApi

import scala.concurrent.Future

@DoNotDiscover
class ApiInfoFeatureSpec extends ApiFeatureSpec {

  "The api endpoint" when {
    "accessed with GET request" should {

      lazy val futureResponse: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api"))

      haveStatusOk(futureResponse)
      haveContentType(ContentTypes.`text/plain(UTF-8)`, futureResponse)

      haveEntityEqualTo(CustomerApi.ApiInfo, futureResponse)
    }
  }

}
