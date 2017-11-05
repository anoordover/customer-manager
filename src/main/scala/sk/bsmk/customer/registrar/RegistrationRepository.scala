package sk.bsmk.customer.registrar

import java.time.LocalDateTime

import org.jooq.DSLContext
import sk.bsmk.customer.Email
import sk.bsmk.customer.persistence.model.tables.Registrations
import Registrations._

import scala.compat.java8.OptionConverters._
import scala.concurrent.{ExecutionContext, Future}

object RegistrationRepository {

  def apply(dsl: DSLContext)(implicit executionContext: ExecutionContext): RegistrationRepository =
    new RegistrationRepository(dsl)

}

class RegistrationRepository(dsl: DSLContext)(implicit executionContext: ExecutionContext) {

  def insert(email: Email, createdAt: LocalDateTime): Future[Unit] = {
    Future {
      dsl
        .insertInto(REGISTRATIONS)
        .set(REGISTRATIONS.EMAIL, email)
        .set(REGISTRATIONS.CREATED_AT, LocalDateTime.now)
        .execute()
    }
  }

  def find(email: Email): Future[Option[StoredRegistration]] = {
    Future {
      dsl
        .selectFrom(REGISTRATIONS)
        .where(REGISTRATIONS.EMAIL.eq(email))
        .fetchOptional()
        .asScala
        .map(record ⇒ StoredRegistration(record.getEmail, record.getCreatedAt))
    }
  }

}

final case class StoredRegistration(email: Email, createdAt: LocalDateTime)
