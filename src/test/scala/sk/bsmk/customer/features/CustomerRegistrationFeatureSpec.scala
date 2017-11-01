package sk.bsmk.customer.features

import akka.http.scaladsl.model.{ContentTypes, StatusCodes}
import sk.bsmk.customer.ApiFeatureSpec
import sk.bsmk.customer.api.{CustomerListResponse, CustomerRegistrationRequest, JsonSupport}

class CustomerRegistrationFeatureSpec extends ApiFeatureSpec {

  "The customers resource" should {
    "register customer for POST request" in {
      val request = CustomerRegistrationRequest("some@mail.com")
      Post("/api/customers", request) ~> route ~> check {
        status shouldEqual StatusCodes.OK
      }
    }
  }

}
