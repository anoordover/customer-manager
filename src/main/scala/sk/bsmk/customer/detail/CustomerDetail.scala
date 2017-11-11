package sk.bsmk.customer.detail

import java.time.LocalDateTime

import sk.bsmk.customer.{Email, Username}

final case class CustomerDetail(username: Username,
                                email: Email,
                                createdAt: LocalDateTime,
                                updatedAt: LocalDateTime,
                                points: Int = 0)
