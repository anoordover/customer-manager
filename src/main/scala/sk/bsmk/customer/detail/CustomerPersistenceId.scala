package sk.bsmk.customer.detail

import sk.bsmk.customer.Username

object CustomerPersistenceId {

  val Prefix = "customer-"

  def asUsername(persistenceId: String): Username = {
    persistenceId.substring(Prefix.length)
  }

  def fromUsername(username: Username): String = {
    Prefix + username
  }

}
