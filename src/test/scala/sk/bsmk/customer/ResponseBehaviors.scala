package sk.bsmk.customer

import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.model.{ContentType, HttpResponse, StatusCodes}
import org.scalatest.{AsyncWordSpec, Matchers}

import scala.concurrent.Future

trait ResponseBehaviors { this: AsyncWordSpec with Matchers ⇒

  def shouldHaveStatusOk(implicit futureResponse: Future[HttpResponse]): Unit =
    "return OK status" in {
      futureResponse map { resp ⇒
        resp.status shouldEqual StatusCodes.OK
      }
    }

  def shouldHaveContentType(contentType: ContentType)(implicit futureHttpResponse: Future[HttpResponse]): Unit = {
    s"return '$contentType' Content-Type" in {
      futureHttpResponse map { resp ⇒
        val headerValue = resp.header[`Content-Type`] map { header ⇒
          header.value()
        }
        headerValue shouldEqual Some(contentType.value)
      }
    }
  }

}
