package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.server.Route
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sk.bsmk.customer.api.{CustomerApi, JsonSupport}

abstract class ApiFeatureSpec extends AsyncWordSpec with Matchers with JsonSupport with BeforeAndAfterAll {

  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val BaseUri = s"http://${CustomerApp.Host}:${CustomerApp.Port}"

  protected val route: Route = CustomerApi.routes

  override protected def beforeAll(): Unit = {
    Http().bindAndHandle(route, CustomerApp.Host, CustomerApp.Port)
  }

  override protected def afterAll(): Unit = {}
}
