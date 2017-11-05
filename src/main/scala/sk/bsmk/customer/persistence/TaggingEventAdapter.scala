package sk.bsmk.customer.persistence

import akka.persistence.journal.{Tagged, WriteEventAdapter}
import sk.bsmk.customer.CustomerActor.CustomerEvent
import sk.bsmk.customer.CustomerActor

class TaggingEventAdapter extends WriteEventAdapter {
  override def manifest(event: Any) = ""

  override def toJournal(event: Any): Any = event match {
    case _: CustomerEvent ⇒
      withTag(event, CustomerActor.Tag)
    case _ ⇒ event
  }

  def withTag(event: Any, tag: String) = Tagged(event, Set(tag))

}
