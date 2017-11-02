package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.http.scaladsl.model.{HttpResponse, ResponseEntity, StatusCodes}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import org.scalatest.{Assertion, AsyncWordSpec, BeforeAndAfterAll, Matchers}
import sk.bsmk.customer.api.{CustomerApi, JsonSupport}

import scala.concurrent.Future

abstract class ApiFeatureSpec
    extends AsyncWordSpec
    with Matchers
    with JsonSupport
    with BeforeAndAfterAll
    with ResponseBehaviors {

  implicit val system: ActorSystem             = ActorSystem()
  implicit val materializer: ActorMaterializer = ActorMaterializer()

  val BaseUri = s"http://${CustomerApp.Host}:${CustomerApp.Port}"

  override protected def beforeAll(): Unit = {
    Http().bindAndHandle(CustomerApi.routes, CustomerApp.Host, CustomerApp.Port)
  }

  protected def checkEntity[T](httpResponse: HttpResponse)(check: T ⇒ Assertion)(
      implicit um: Unmarshaller[ResponseEntity, T]): Future[Assertion] = {
    val unmarshaledF: Future[T] = Unmarshal(httpResponse.entity).to[T]
    unmarshaledF flatMap (a ⇒ check(a))
  }

}
