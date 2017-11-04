package sk.bsmk.customer.repository

import java.util.concurrent.Executors

import org.jooq._
import sk.bsmk.customer.Email
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.persistence.model.Tables._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
import scala.compat.java8.OptionConverters._

class CustomerRepository(
    dsl: DSLContext
) {

  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def insert(customerDetail: CustomerDetail): Future[Int] = {
    Future {
      dsl
        .insertInto(CUSTOMERS)
        .set(CUSTOMERS.UUID, customerDetail.uuid)
        .set(CUSTOMERS.EMAIL, customerDetail.email)
        .execute()
    }
  }

  def update(customerDetail: CustomerDetail): Future[Int] = {
    Future {
      dsl
        .update(CUSTOMERS)
        .set(CUSTOMERS.EMAIL, customerDetail.email)
        .where(CUSTOMERS.UUID.eq(customerDetail.uuid))
        .execute()
    }
  }

  def find(email: Email): Future[Option[CustomerDetail]] = {
    Future {
      dsl
        .selectFrom(CUSTOMERS)
        .where(CUSTOMERS.EMAIL.eq(email))
        .fetchOptional()
        .asScala
        .map(record ⇒ CustomerDetail(record.getUuid, record.getEmail))
    }
  }

}
