package sk.bsmk.customer

import org.slf4j.LoggerFactory
import sk.bsmk.customer.mailman.{MessagingService, MessagingServiceData}

import scala.concurrent.{Future, Promise}

class MockMessagingService extends MessagingService {

  private val log = LoggerFactory.getLogger(this.getClass)

  private var received: Promise[MessagingServiceData] = Promise()

  override def sendMessage(data: MessagingServiceData): Unit = {
    log.info("Received {}", data)
    received.success(data)
  }

  def data(): Future[MessagingServiceData] = {
    received.future
  }

  def reset(): Unit = {
    received = Promise()
  }

}
