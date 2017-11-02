package sk.bsmk.customer

import akka.actor.ActorSystem
import akka.event.Logging
import akka.pattern.ask
import akka.util.Timeout
import sk.bsmk.customer.api.CustomerApi
import sk.bsmk.customer.registration.CustomerRegistrator

import scala.concurrent.Await
import scala.concurrent.duration._
import scala.language.postfixOps

object CustomerApp extends App {

  lazy val Host = "localhost"
  lazy val Port = 8080

  val system = ActorSystem("customer-manager")
  val log    = Logging.getLogger(system, this)

//  val persistenceId = "1"
//
//  val customer = system.actorOf(CustomerActor.props(persistenceId))
//
//  customer ! RegisterCustomer("new@customer.com")
//
//  customer ! AddVoucher("a")
//  customer ! AddVoucher("b")
//  customer ! AddVoucher("d")
//  customer ! AddVoucher("e")
//
//  implicit val timeout: Timeout = Timeout(5 seconds)
//  Await.result(customer ? GetState, 5.seconds) match {
//    case Some(actual) ⇒ log.info("Customer with id={} has state={}", persistenceId, actual)
//    case None         ⇒ log.error("Customer with id={} with none state", persistenceId)
//    case _            ⇒ log.error("Unknown return from GetState")
//  }
//
//  Await.result(system.terminate(), 5 seconds)

  val registrator = system.actorOf(CustomerRegistrator.props)
  val customerApi = CustomerApi(registrator)

  customerApi.startServer(Host, Port, system)

}
