package sk.bsmk.customer.registrar

import sk.bsmk.customer.{Email, Username}

final case class RegistrationData(username: Username, email: Email)
