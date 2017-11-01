package sk.bsmk.customer.features

import akka.http.scaladsl.model.StatusCodes
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerApi

class ApiInfoFeatureSpec extends ApiFeatureSpec {

  "The api" should {
    "return an info for GET requests" in {
      Get("/api") ~> route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual CustomerApi.ApiInfo
      }
    }
  }

}
