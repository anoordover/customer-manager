package sk.bsmk.customer.registrar

import java.util.UUID

trait CustomerPersistenceIdGenerator {

  def generate(): String

}

object CustomerPersistenceUuidGenerator extends CustomerPersistenceIdGenerator {

  override def generate(): String = UUID.randomUUID().toString

}
