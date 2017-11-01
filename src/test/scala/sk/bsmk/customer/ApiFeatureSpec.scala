package sk.bsmk.customer

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}
import sk.bsmk.customer.api.{CustomerApi, JsonSupport}

abstract class ApiFeatureSpec extends WordSpec with Matchers with ScalatestRouteTest with JsonSupport {

  protected val route = CustomerApi.routes

}
