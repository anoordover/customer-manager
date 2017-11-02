package sk.bsmk.customer.features

import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{ContentTypes, HttpRequest, HttpResponse}
import sk.bsmk.customer.ApiFeatureSpec

import scala.concurrent.Future

class CustomerListFeatureSpec extends ApiFeatureSpec {

  "The customer list endpoint" when {
    "accessed with GET request" should {
      implicit val futureResp: Future[HttpResponse] = Http().singleRequest(HttpRequest(uri = s"$BaseUri/api/customers"))

      shouldHaveStatusOk
      shouldHaveContentType(ContentTypes.`application/json`)

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
