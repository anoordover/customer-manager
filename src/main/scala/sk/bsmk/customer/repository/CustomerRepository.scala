package sk.bsmk.customer.repository

import java.util.UUID
import javax.sql.DataSource

import org.jooq._
import org.jooq.impl._
import org.jooq.impl.DSL._
import org.jooq.scalaextensions.Conversions._

import sk.bsmk.customer.Customer
import sk.bsmk.customer.persistence.model.Tables._

import scala.concurrent.Future

class CustomerRepository(
    dsl: DSLContext
) {

  def find(uuid: UUID): Future[Customer] = {

    dsl.fetchAsync(CUSTOMERS, CUSTOMERS.UUID.eq(uuid))





  }

}
