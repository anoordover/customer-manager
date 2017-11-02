package sk.bsmk.customer.api

import java.util.UUID

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import spray.json.{DefaultJsonProtocol, JsString, JsValue, JsonFormat}

trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {

  implicit object UuidFormat extends JsonFormat[UUID] {
    override def write(obj: UUID): JsValue = JsString(obj.toString)

    override def read(json: JsValue): UUID = json match {
      case JsString(str) ⇒ UUID.fromString(str)
      case _             ⇒ spray.json.deserializationError("UUID expected.")
    }
  }

  implicit val customerRegistrationRequestFormat  = jsonFormat1(CustomerRegistrationRequest)
  implicit val customerRegistrationResponseFormat = jsonFormat2(CustomerRegistrationResponse)

  implicit val customerListResponseItemFormat = jsonFormat2(CustomerListResponseItem)
  implicit val customerListResponseFormat     = jsonFormat1(CustomerListResponse)

}
