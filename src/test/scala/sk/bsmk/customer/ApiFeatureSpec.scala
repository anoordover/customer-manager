package sk.bsmk.customer

import akka.http.scaladsl.testkit.ScalatestRouteTest
import org.scalatest.{Matchers, WordSpec}

abstract class ApiFeatureSpec extends WordSpec with Matchers with ScalatestRouteTest {}
