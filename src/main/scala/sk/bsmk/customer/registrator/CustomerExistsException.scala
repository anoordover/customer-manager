package sk.bsmk.customer.registrator

case class CustomerExistsException(email: String) extends Exception
