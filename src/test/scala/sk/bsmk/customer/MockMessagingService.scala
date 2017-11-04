package sk.bsmk.customer

import sk.bsmk.customer.mailman.{MessagingService, MessagingServiceData}

import scala.concurrent.{Future, Promise}

class MockMessagingService extends MessagingService {

  var received: Promise[MessagingServiceData] = Promise()

  override def sendMessage(data: MessagingServiceData): Unit = {
    received = Promise.successful(data)
  }

  def pop(): Future[MessagingServiceData] = {
    val future = received.future
    received = Promise()
    future
  }

}
