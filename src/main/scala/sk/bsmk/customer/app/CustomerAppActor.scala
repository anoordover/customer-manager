package sk.bsmk.customer.app

import akka.actor.{Actor, ActorLogging, ActorSystem, Props}
import akka.http.scaladsl.Http
import akka.http.scaladsl.Http.ServerBinding
import akka.pattern.pipe
import akka.stream.ActorMaterializer
import sk.bsmk.customer.api.CustomerApi
import sk.bsmk.customer.app.CustomerAppActor.{CustomerAppServerStarted, StartCustomerAppServer, StopCustomerAppServer}
import sk.bsmk.customer.registrator.RegistratorActor

import scala.concurrent.{ExecutionContextExecutor, Future}
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

  var bindingOption: Option[Future[ServerBinding]] = None

  override def receive: PartialFunction[Any, Unit] = {

    case StartCustomerAppServer ⇒
      log.debug("Starting server")
      val registrator = context.actorOf(RegistratorActor.props)
      val customerApi = new CustomerApi(registrator)
      val future = Http()
        .bindAndHandle(customerApi.routes, CustomerAppActor.Host, CustomerAppActor.Port)

      bindingOption = Some(future)

      future
        .map(_ ⇒ CustomerAppServerStarted)
        .pipeTo(sender())
        .onComplete(_ ⇒ log.debug("Server started"))

    case StopCustomerAppServer ⇒
      log.debug("Stopping server")
      bindingOption match {
        case None ⇒ log.error("No future server binding found. Was server started?")
        case Some(binding) ⇒
          binding
            .flatMap(_.unbind())
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
