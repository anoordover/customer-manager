package sk.bsmk.customer.mailman

trait MessagingService {

  def sendMessage(endpoint: String, message: String)

}
