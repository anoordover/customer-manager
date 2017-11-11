package sk.bsmk.customer.representative

import com.typesafe.scalalogging.LazyLogging
import sk.bsmk.customer.bookkeeper.Bookkeeper
import sk.bsmk.customer.bookkeeper.Bookkeeper.ProvideDetail
import sk.bsmk.customer.detail.CustomerDetail
import sk.bsmk.customer.registrar.Registrar.RegisterCustomer
import sk.bsmk.customer.registrar.{Registrar, Stored}
import sk.bsmk.customer.representative.Representative.{DetailRequest, RegistrationRequest}
import sk.bsmk.customer.{Email, RegistrationData}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.Future

object Representative {

  final case class RegistrationRequest(data: RegistrationData)
  final case class DetailRequest(email: Email)

  def apply(registrar: Registrar, bookkeeper: Bookkeeper): Representative = new Representative(registrar, bookkeeper)

}

class Representative(registrar: Registrar, bookkeeper: Bookkeeper) extends LazyLogging {

  def receiveRegistration(request: RegistrationRequest): Future[Option[CustomerDetail]] = {

    logger.info("Processing registration request {}", request)
    registrar
      .register(RegisterCustomer(request.data))
      .flatMap {
        case Stored ⇒ bookkeeper.provideDetail(ProvideDetail(request.data.email))
      }
  }

  def provideDetail(request: DetailRequest): Future[Option[CustomerDetail]] = {
    logger.info("Processing detail request for {}", request)
    bookkeeper.provideDetail(ProvideDetail(request.email))
  }

}
