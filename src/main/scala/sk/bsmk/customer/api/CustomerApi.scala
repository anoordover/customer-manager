package sk.bsmk.customer.api

import akka.http.scaladsl.server.{HttpApp, Route}

object CustomerApi extends HttpApp {

  val ApiInfo: String = "Customer API"

  override def routes: Route = get {
    pathSingleSlash {
      complete {
        "Captain on the bridge!"
      }
    } ~
      path("ping") {
        complete("PONG!")
      }
  }

}
