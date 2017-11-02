package sk.bsmk.customer

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.util.Timeout
import akka.pattern.ask
import org.scalatest.{BeforeAndAfterAll, Suite}
import sk.bsmk.customer.app.CustomerAppActor
import sk.bsmk.customer.app.CustomerAppActor.{StartCustomerAppServer, StopCustomerAppServer}

import scala.concurrent.Await

trait RunningApp extends BeforeAndAfterAll { this: Suite ⇒

  val appActorSystem: ActorSystem = ActorSystem("app-system")
  private val customerApp         = appActorSystem.actorOf(CustomerAppActor.props)

  override protected def beforeAll(): Unit = {
    implicit val timeout: Timeout = Timeout(10.seconds)
    Await.result(customerApp ? StartCustomerAppServer, timeout.duration)
  }

  override protected def afterAll(): Unit = {
    customerApp ! StopCustomerAppServer
  }

}
