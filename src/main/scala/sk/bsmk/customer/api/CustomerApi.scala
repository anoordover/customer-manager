package sk.bsmk.customer.api

import java.util.UUID

import akka.actor.ActorRef
import akka.http.scaladsl.server.{HttpApp, Route}
import sk.bsmk.customer.registration.RegisterCustomer

object CustomerApi {

  val ApiInfo: String = "Customer API"

  def apply(registrator: ActorRef) = new CustomerApi(registrator)

}

class CustomerApi(
    registrator: ActorRef
) extends HttpApp
    with JsonSupport {

  override def routes: Route = pathPrefix("api") {
    pathEndOrSingleSlash {
      get {
        complete {
          CustomerApi.ApiInfo
        }
      }
    } ~
      pathPrefix("customers") {
        post {
          entity(as[CustomerRegistrationRequest]) { registrationRequest ⇒
            registrator ! RegisterCustomer(registrationRequest.email)
            complete(registrationRequest.email)
          }
        } ~
          get {
            complete {
              CustomerListResponse(
                List(
                  CustomerListResponseItem(UUID.randomUUID(), "somemail"),
                  CustomerListResponseItem(UUID.randomUUID(), "somemail")
                )
              )
            }
          }
      }
  }

}
