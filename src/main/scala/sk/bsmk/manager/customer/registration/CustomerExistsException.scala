package sk.bsmk.manager.customer.registration

case class CustomerExistsException(email: String) extends Exception
