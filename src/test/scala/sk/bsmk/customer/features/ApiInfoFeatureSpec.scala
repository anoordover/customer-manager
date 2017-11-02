package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, HttpResponse}
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerApi

import scala.concurrent.Future

class ApiInfoFeatureSpec extends ApiFeatureSpec {

  "The api endpoint" when {
    "accessed with GET request" should {
      implicit val futureResp: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api"))

      shouldHaveStatusOk

      shouldHaveContentType(ContentTypes.`text/plain(UTF-8)`)

      s"return '${CustomerApi.ApiInfo}' content" in {
        futureResp flatMap { resp ⇒
          checkEntity[String](resp) { entity ⇒
            entity shouldEqual CustomerApi.ApiInfo
          }
        }
      }

    }
  }

}
