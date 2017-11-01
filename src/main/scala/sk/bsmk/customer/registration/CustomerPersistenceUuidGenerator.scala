package sk.bsmk.customer.registration

import java.util.UUID

class CustomerPersistenceUuidGenerator extends CustomerPersistenceIdGenerator {

  override def generate(): String = UUID.randomUUID().toString

}
