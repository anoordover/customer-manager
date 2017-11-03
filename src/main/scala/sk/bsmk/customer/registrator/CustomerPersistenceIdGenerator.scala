package sk.bsmk.customer.registrator

trait CustomerPersistenceIdGenerator {

  def generate(): String

}
