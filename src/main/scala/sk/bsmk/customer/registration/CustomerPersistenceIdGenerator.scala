package sk.bsmk.customer.registration

trait CustomerPersistenceIdGenerator {

  def generate(): String

}
