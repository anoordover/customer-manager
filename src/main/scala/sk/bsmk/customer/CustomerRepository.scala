package sk.bsmk.customer

import java.time.LocalDateTime

import org.jooq._
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.persistence.model.Tables._

import scala.compat.java8.OptionConverters._
import scala.concurrent.{ExecutionContext, Future}

object CustomerRepository {

  def apply(dsl: DSLContext)(implicit executionContext: ExecutionContext) = new CustomerRepository(dsl)

}

class CustomerRepository(
    dsl: DSLContext
)(implicit executionContext: ExecutionContext) {

  def insert(detail: CustomerDetail): Future[Int] = {
    Future {
      dsl
        .insertInto(CUSTOMERS)
        .set(CUSTOMERS.USERNAME, detail.username)
        .set(CUSTOMERS.EMAIL, detail.email)
        .set(CUSTOMERS.CREATED_AT, detail.createdAt)
        .set(CUSTOMERS.UPDATED_AT, detail.createdAt)
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

  def find(username: Username): Future[Option[CustomerDetail]] = {
    Future {
      dsl
        .selectFrom(CUSTOMERS)
        .where(CUSTOMERS.USERNAME.eq(username))
        .fetchOptional()
        .asScala
        .map(record ⇒ CustomerDetail(record.getUsername, record.getEmail, record.getCreatedAt, record.getUpdatedAt, 0))
    }
  }

}
