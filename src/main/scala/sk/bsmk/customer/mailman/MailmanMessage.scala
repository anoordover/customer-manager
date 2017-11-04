package sk.bsmk.customer.mailman

import sk.bsmk.customer.Email

sealed trait MailmanMessage

case class EmailAlreadyExists(email: Email)     extends MailmanMessage
case class RegistrationSuccessful(email: Email) extends MailmanMessage
