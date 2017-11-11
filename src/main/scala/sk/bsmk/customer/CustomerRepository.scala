package sk.bsmk.customer

import java.time.LocalDateTime
import java.util.UUID
import java.util.concurrent.Executors

import org.jooq._
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.persistence.model.Tables._

import scala.compat.java8.OptionConverters._
import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}

object CustomerRepository {

  def apply(dsl: DSLContext)(implicit executionContext: ExecutionContext) = new CustomerRepository(dsl)

}

class CustomerRepository(
    dsl: DSLContext
)(implicit executionContext: ExecutionContext) {

  def insert(uuid: UUID, email: Email): Future[Int] = {
    Future {
      dsl
        .insertInto(CUSTOMERS)
        .set(CUSTOMERS.UUID, uuid)
        .set(CUSTOMERS.EMAIL, email)
        .set(CUSTOMERS.CREATED_AT, LocalDateTime.now())
        .set(CUSTOMERS.UPDATED_AT, LocalDateTime.now())
        .execute()
    }
  }

//  def update(customerDetail: CustomerDetail): Future[Int] = {
//    Future {
//      dsl
//        .update(CUSTOMERS)
//        .set(CUSTOMERS.EMAIL, customerDetail.email)
//        .where(CUSTOMERS.UUID.eq(customerDetail.uuid))
//        .execute()
//    }
//  }

  def find(email: Email): Future[Option[CustomerDetail]] = {
    Future {
      dsl
        .selectFrom(CUSTOMERS)
        .where(CUSTOMERS.EMAIL.eq(email))
        .fetchOptional()
        .asScala
        .map(record ⇒ CustomerDetail(record.getEmail, 0))
    }
  }

}
