package sk.bsmk.customer.app

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import sk.bsmk.customer.api.CustomerApi
import sk.bsmk.customer.app.CustomerAppActor.StopCustomerApp
import sk.bsmk.customer.registration.CustomerRegistrator

import scala.concurrent.{ExecutionContextExecutor, Future}

object CustomerAppActor {
  lazy val Host = "localhost"
  lazy val Port = 8080

  object StopCustomerApp

  def props: Props = Props[CustomerAppActor]

}

class CustomerAppActor extends Actor with ActorLogging {

  implicit val system: ActorSystem                        = context.system
  implicit val materializer: ActorMaterializer            = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var futureBindingOption: Option[Future[ServerBinding]] = None

  override def preStart(): Unit = {

    val registrator = system.actorOf(CustomerRegistrator.props)
    val customerApi = new CustomerApi(registrator)

    futureBindingOption = Some(Http().bindAndHandle(customerApi.routes, CustomerAppActor.Host, CustomerAppActor.Port))

  }

  override def receive: PartialFunction[Any, Unit] = {

    case StopCustomerApp ⇒
      futureBindingOption match {
        case None ⇒ log.error("No future server binding found. Is was App started?")
        case Some(futureBinding) ⇒
          futureBinding
            .flatMap(_.unbind())
            .onComplete(_ ⇒ system.terminate())

      }
    case message ⇒ log.error("App actor received a message: {}", message)
  }

}
