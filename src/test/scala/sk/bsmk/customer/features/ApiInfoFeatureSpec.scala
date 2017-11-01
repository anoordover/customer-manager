package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpRequest, StatusCodes}
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerApi

class ApiInfoFeatureSpec extends ApiFeatureSpec {

  "The api" should {
    "return an info for GET requests" in {
      Http().singleRequest(HttpRequest(uri = s"$BaseUri/api")) map { resp =>
        resp.status shouldEqual StatusCodes.OK
//        resp.entity
      }
    }
  }

}
