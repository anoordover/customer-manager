package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpHeader, HttpRequest, StatusCodes}
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerApi
import akka.http.scaladsl.model.headers._

class ApiInfoFeatureSpec extends ApiFeatureSpec {

  "The api endpoint" when {
    "accessed with GET request" should {
      val futureResp = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api"))

      "return OK" in {
        futureResp map { resp ⇒
          resp.status shouldEqual StatusCodes.OK
        }
      }

      s"return '${ContentTypes.`text/plain(UTF-8)`}'" in {
        futureResp map { resp ⇒
          val headerValue = resp.header[`Content-Type`] map { header ⇒
            header.value()
          }
          headerValue shouldEqual Some(ContentTypes.`text/plain(UTF-8)`.value)
        }
      }

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
