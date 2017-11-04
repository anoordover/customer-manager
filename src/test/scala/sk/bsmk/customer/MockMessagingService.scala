package sk.bsmk.customer

import sk.bsmk.customer.mailman.MessagingService

class MockMessagingService extends MessagingService {

  var received: Option[(String, String)] = None

  override def sendMessage(endpoint: String, message: String): Unit = {
    received = Some((endpoint, message))
  }

  def pop(): (String, String) = {
    val data = received.get
    received = None
    data
  }

}
