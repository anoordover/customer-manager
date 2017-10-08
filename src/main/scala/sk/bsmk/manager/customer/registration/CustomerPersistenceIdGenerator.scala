package sk.bsmk.manager.customer.registration

trait CustomerPersistenceIdGenerator {

  def generate(): String

}
