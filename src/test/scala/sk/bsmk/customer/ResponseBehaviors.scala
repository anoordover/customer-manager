package sk.bsmk.customer

import akka.http.scaladsl.model.{HttpResponse, StatusCodes}
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.Future

trait ResponseBehaviors { this: AsyncWordSpec with Matchers ⇒

  def shouldReturnOk(futureResponse: Future[HttpResponse]): Unit =
    "return OK" in {
      futureResponse map { resp ⇒
        resp.status shouldEqual StatusCodes.OK
      }
    }

}
