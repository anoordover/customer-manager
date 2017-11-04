package sk.bsmk.customer.mailman

final case class MessagingServiceData(endpoint: String, message: String)

trait MessagingService {

  def sendMessage(data: MessagingServiceData)

}
