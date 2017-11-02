package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.stream.ActorMaterializer
import org.scalatest.{AsyncWordSpec, Matchers}
import sk.bsmk.customer.api.JsonSupport
import sk.bsmk.customer.app.CustomerAppActor

abstract class ApiFeatureSpec extends AsyncWordSpec with Matchers with JsonSupport with ResponseBehaviors {

  implicit val system: ActorSystem             = ActorSystem("test-system")
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val BaseUri = s"http://${CustomerAppActor.Host}:${CustomerAppActor.Port}"

}
