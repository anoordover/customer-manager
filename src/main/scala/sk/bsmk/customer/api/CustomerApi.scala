package sk.bsmk.customer.api

import java.util.UUID

import akka.http.scaladsl.server.{HttpApp, Route}

object CustomerApi extends HttpApp with JsonSupport {

  val ApiInfo: String = "Customer API"

  override def routes: Route = pathPrefix("api") {
    pathEndOrSingleSlash {
      get {
        complete {
          ApiInfo
        }
      }
    } ~
      pathPrefix("customers") {
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
