package sk.bsmk.customer

sealed trait CustomerEvent

case class CustomerRegistered(email: String) extends CustomerEvent
case class VoucherAdded(voucherId: String)   extends CustomerEvent
