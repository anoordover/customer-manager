package sk.bsmk.customer.registrator

import java.util.UUID

trait CustomerPersistenceIdGenerator {

  def generate(): String

}

object CustomerPersistenceUuidGenerator extends CustomerPersistenceIdGenerator {

  override def generate(): String = UUID.randomUUID().toString

}
