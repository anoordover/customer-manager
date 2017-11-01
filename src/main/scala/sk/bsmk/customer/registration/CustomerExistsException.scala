package sk.bsmk.customer.registration

case class CustomerExistsException(email: String) extends Exception
