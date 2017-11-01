package sk.bsmk.customer.api

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.DefaultJsonProtocol

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit val customerListResponseItemFormat = jsonFormat2(CustomerListResponseItem)
  implicit val customerListResponseFormat     = jsonFormat1(CustomerListResponse)

}
