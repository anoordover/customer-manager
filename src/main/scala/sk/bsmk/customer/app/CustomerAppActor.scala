package sk.bsmk.customer.app

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.stream.ActorMaterializer
import sk.bsmk.customer.api.CustomerApi
import sk.bsmk.customer.app.CustomerAppActor.{CustomerAppServerStarted, StartCustomerAppServer, StopCustomerAppServer}
import sk.bsmk.customer.registration.CustomerRegistrator

import scala.concurrent.duration._
import scala.concurrent.{Await, ExecutionContextExecutor}
import scala.util.{Failure, Success}

object CustomerAppActor {
  lazy val Host = "localhost"
  lazy val Port = 8080

  object StartCustomerAppServer
  object StopCustomerAppServer
  object CustomerAppServerStarted

  def props: Props = Props[CustomerAppActor]

}

class CustomerAppActor extends Actor with ActorLogging {

  implicit val system: ActorSystem                        = context.system
  implicit val materializer: ActorMaterializer            = ActorMaterializer()
  implicit val executionContext: ExecutionContextExecutor = system.dispatcher

  var bindingOption: Option[ServerBinding] = None

  override def receive: PartialFunction[Any, Unit] = {

    case StartCustomerAppServer ⇒
      val registrator = context.actorOf(CustomerRegistrator.props)
      val customerApi = new CustomerApi(registrator)

      log.debug("Starting server")
      bindingOption = Some(
        Await.result(Http().bindAndHandle(customerApi.routes, CustomerAppActor.Host, CustomerAppActor.Port), 10.seconds)
      )
      log.debug("Server started")
      sender() ! CustomerAppServerStarted

    case StopCustomerAppServer ⇒
      bindingOption match {
        case None ⇒ log.error("No future server binding found. Is was App started?")
        case Some(binding) ⇒
          binding
            .unbind()
            .onComplete {
              case Success(_) ⇒ system.terminate()
              case Failure(e) ⇒
                log.error("Problem with unbinding server", e)
                system.terminate()
            }

      }
    case message ⇒ log.error("App actor received unknown message: {}", message)
  }

}
