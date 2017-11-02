package sk.bsmk.customer.api

import java.util.UUID

import sk.bsmk.customer.Email

final case class CustomerRegistrationResponse(
    uuid: UUID,
    email: Email
)
