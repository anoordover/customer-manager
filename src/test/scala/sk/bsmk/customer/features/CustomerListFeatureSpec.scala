package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.HttpRequest
import sk.bsmk.customer.ApiFeatureSpec

class CustomerListFeatureSpec extends ApiFeatureSpec {

  "The customer list endpoint" when {
    "accessed with GET request" should {
      val futureResp = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api/customers"))

      shouldReturnOk(futureResp)

    }
  }
  //  "The customers resource" should {
//    "return list of customers for GET requests" in {
//      Get("/api/customers") ~> route ~> check {
//        status shouldEqual StatusCodes.OK
//        contentType shouldEqual ContentTypes.`application/json`
//        val response = responseAs[CustomerListResponse]
//        response.items should have size 2
//      }
//    }
//  }

}
