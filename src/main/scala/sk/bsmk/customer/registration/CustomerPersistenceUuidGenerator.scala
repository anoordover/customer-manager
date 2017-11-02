package sk.bsmk.customer.registration

import java.util.UUID

object CustomerPersistenceUuidGenerator extends CustomerPersistenceIdGenerator {

  override def generate(): String = UUID.randomUUID().toString

}
