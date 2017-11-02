package sk.bsmk.customer

import scala.concurrent.duration._
import akka.actor.ActorSystem
import akka.pattern.ask
import akka.util.Timeout
import org.scalatest.{BeforeAndAfterAll, Suites}
import sk.bsmk.customer.app.CustomerAppActor
import sk.bsmk.customer.app.CustomerAppActor.{StartCustomerAppServer, StopCustomerAppServer}
import sk.bsmk.customer.features.{ApiInfoFeatureSpec, CustomerListFeatureSpec, CustomerRegistrationFeatureSpec}

import scala.concurrent.Await

class CustomerAppFeaturesSuite
    extends Suites(
      new ApiInfoFeatureSpec,
      new CustomerListFeatureSpec
//      ,
//      new CustomerRegistrationFeatureSpec
    )
    with BeforeAndAfterAll {

  implicit val system: ActorSystem = ActorSystem("app-system")

  private val customerApp = system.actorOf(CustomerAppActor.props)

  override protected def beforeAll(): Unit = {
    implicit val timeout: Timeout = Timeout(10.seconds)
    Await.result(customerApp ? StartCustomerAppServer, 10.seconds)
  }

  override protected def afterAll(): Unit = {
    customerApp ! StopCustomerAppServer
  }

}
