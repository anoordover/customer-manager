package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sk.bsmk.customer.api.JsonSupport
import sk.bsmk.customer.app.CustomerAppActor
import sk.bsmk.customer.app.CustomerAppActor.StopCustomerApp

abstract class ApiFeatureSpec
    extends AsyncWordSpec
    with Matchers
    with JsonSupport
    with BeforeAndAfterAll
    with ResponseBehaviors {

  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  private val customerApp = system.actorOf(CustomerAppActor.props)

  val BaseUri = s"http://${CustomerAppActor.Host}:${CustomerAppActor.Port}"

  override protected def beforeAll(): Unit = {}

  override protected def afterAll(): Unit = {
    customerApp ! StopCustomerApp
  }

}
