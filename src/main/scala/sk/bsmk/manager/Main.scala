package sk.bsmk.manager

import akka.actor.ActorSystem
import sk.bsmk.manager.customer.{AddVoucher, CustomerActor, RegisterCustomer}

import scala.concurrent.Await
import scala.concurrent.duration._

object Main extends App {

  val system = ActorSystem("customer-manager")

  val customer = system.actorOf(CustomerActor.props("1"))

  customer ! RegisterCustomer("new@customer.com")

  customer ! AddVoucher("a")
  customer ! AddVoucher("b")
  customer ! AddVoucher("d")
  customer ! AddVoucher("e")

  customer ! "print"

  Thread.sleep(1000)

  Await.result(system.terminate(), 5.seconds)

}
