package sk.bsmk.customer.registrar

case class CustomerExistsException(email: String) extends Exception
