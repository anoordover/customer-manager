package sk.bsmk.customer.features

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.CustomerListResponse

class CustomerListFeatureSpec extends ApiFeatureSpec {

  "The customers resource" should {
    "return list of customers for GET requests" in {
      Get("/api/customers") ~> route ~> check {
        status shouldEqual StatusCodes.OK
        contentType shouldEqual ContentTypes.`application/json`
        val response = responseAs[CustomerListResponse]
        response.items should have size 2
      }
    }
  }

}
