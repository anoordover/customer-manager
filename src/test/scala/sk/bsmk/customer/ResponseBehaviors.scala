package sk.bsmk.customer

import akka.http.scaladsl.model.headers.`Content-Type`
import akka.http.scaladsl.model.{ContentType, HttpResponse, ResponseEntity, StatusCodes}
import akka.http.scaladsl.unmarshalling.{Unmarshal, Unmarshaller}
import akka.stream.ActorMaterializer
import org.scalatest.{Assertion, AsyncWordSpec, Matchers}

import scala.concurrent.Future

trait ResponseBehaviors { this: AsyncWordSpec with Matchers ⇒

  def haveStatusOk(futureResponse: ⇒ Future[HttpResponse]): Unit =
    "return OK status" in {
      futureResponse map { resp ⇒
        resp.status shouldEqual StatusCodes.OK
      }
    }

  def haveContentType(contentType: ContentType, futureHttpResponse: ⇒ Future[HttpResponse]): Unit = {
    s"return '$contentType' Content-Type" in {
      futureHttpResponse map { resp ⇒
        val headerValue = resp.header[`Content-Type`] map { header ⇒
          header.value()
        }
        headerValue shouldEqual Some(contentType.value)
      }
    }
  }

  def haveEntityEqualTo[T](expected: T, futureHttpResponse: ⇒ Future[HttpResponse])(
      implicit
      materializer: ActorMaterializer,
      um: Unmarshaller[ResponseEntity, T]): Unit = {
    s"return '$expected' content" in {
      futureHttpResponse flatMap { resp ⇒
        checkEntity[T](resp) { entity ⇒
          entity shouldEqual expected
        }
      }
    }
  }

  def haveEntity[T](description: String, futureHttpResponse: ⇒ Future[HttpResponse])(
      check: T ⇒ Assertion)(implicit materializer: ActorMaterializer, um: Unmarshaller[ResponseEntity, T]): Unit = {
    description in {
      futureHttpResponse flatMap { resp ⇒
        checkEntity[T](resp)(check)
      }
    }
  }

  private def checkEntity[T](httpResponse: HttpResponse)(check: T ⇒ Assertion)(
      implicit materializer: ActorMaterializer,
      um: Unmarshaller[ResponseEntity, T]): Future[Assertion] = {
    val unmarshaledF: Future[T] = Unmarshal(httpResponse.entity).to[T]
    unmarshaledF flatMap (a ⇒ check(a))
  }

}
