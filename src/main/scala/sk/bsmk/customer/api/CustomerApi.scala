package sk.bsmk.customer.api

import akka.http.scaladsl.server.{HttpApp, Route}

object CustomerApi extends HttpApp {

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
            "foo"
          }
        }
      }
  }

}
