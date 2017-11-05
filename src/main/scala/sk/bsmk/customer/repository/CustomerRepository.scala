package sk.bsmk.customer.repository

import java.util.UUID
import java.util.concurrent.Executors

import org.jooq._
import sk.bsmk.customer.Email
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.persistence.model.Tables._

import scala.concurrent.{ExecutionContext, ExecutionContextExecutorService, Future}
import scala.compat.java8.OptionConverters._

object CustomerRepository {

  def apply(dsl: DSLContext) = new CustomerRepository(dsl)

}

class CustomerRepository(
    dsl: DSLContext
) {

  implicit val ec: ExecutionContextExecutorService =
    ExecutionContext.fromExecutorService(Executors.newCachedThreadPool())

  def insert(uuid: UUID, email: Email): Future[Int] = {
    Future {
      dsl
        .insertInto(CUSTOMERS)
        .set(CUSTOMERS.UUID, uuid)
        .set(CUSTOMERS.EMAIL, email)
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
