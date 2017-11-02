package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sk.bsmk.customer.api.{CustomerApi, JsonSupport}
import sk.bsmk.customer.registration.CustomerRegistrator

abstract class ApiFeatureSpec
    extends AsyncWordSpec
    with Matchers
    with JsonSupport
    with BeforeAndAfterAll
    with ResponseBehaviors {

  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val registrator = system.actorOf(CustomerRegistrator.props)
  val customerApi = CustomerApi(registrator)

  val BaseUri = s"http://${CustomerApp.Host}:${CustomerApp.Port}"

  override protected def beforeAll(): Unit = {
    Http().bindAndHandle(customerApi.routes, CustomerApp.Host, CustomerApp.Port)
  }

}
