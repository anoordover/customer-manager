package sk.bsmk.customer.registrar

import sk.bsmk.customer.detail.CustomerDetail

sealed trait RegistrationResult

final case class Stored(customerDetail: CustomerDetail) extends RegistrationResult
