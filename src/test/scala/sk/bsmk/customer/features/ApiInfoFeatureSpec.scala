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
      implicit val futureResp: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api"))

      haveStatusOk
      haveContentType(ContentTypes.`text/plain(UTF-8)`)
      haveEntityEqualTo(CustomerApi.ApiInfo)
    }
  }

}
