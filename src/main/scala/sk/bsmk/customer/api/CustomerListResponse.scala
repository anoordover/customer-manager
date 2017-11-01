package sk.bsmk.customer.api

import java.util.UUID

import sk.bsmk.customer.Email

final case class CustomerListResponse(items: Seq[CustomerListResponseItem])

final case class CustomerListResponseItem(uuid: UUID, email: Email)
