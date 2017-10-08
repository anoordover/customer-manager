package sk.bsmk.manager.customer

import akka.persistence.journal.EventAdapter

class CustomerEventAdapter extends EventAdapter  {

  override def manifest(event: Any) = ???

  override def toJournal(event: Any) = ???

  override def fromJournal(event: Any, manifest: String) = ???

}
