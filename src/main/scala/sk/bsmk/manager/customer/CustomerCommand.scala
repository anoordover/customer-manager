package sk.bsmk.manager.customer

sealed trait CustomerCommand

final case class RegisterCustomer(email: String) extends CustomerCommand
final case class AddVoucher(voucherId: String) extends CustomerCommand

case object GetState
