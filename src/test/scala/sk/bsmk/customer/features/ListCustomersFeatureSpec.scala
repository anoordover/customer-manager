package sk.bsmk.customer.features

import akka.http.scaladsl.model.StatusCodes
import sk.bsmk.customer.ApiFeatureSpec

class ListCustomersFeatureSpec extends ApiFeatureSpec {

  "The customers resource" should {
    "return list of customers for GET requests" in {
      Get("/api/customers") ~> route ~> check {
        status shouldEqual StatusCodes.OK
        responseAs[String] shouldEqual "foo"
      }
    }
  }

}
