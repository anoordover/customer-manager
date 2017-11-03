package sk.bsmk.customer.registrator

import java.util.UUID

object CustomerPersistenceUuidGenerator extends CustomerPersistenceIdGenerator {

  override def generate(): String = UUID.randomUUID().toString

}
