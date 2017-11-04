package sk.bsmk.customer.features

import org.scalatest.{FeatureSpec, GivenWhenThen}
import sk.bsmk.customer.app.CustomerAppActor

trait CustomerFeatureSpec extends FeatureSpec with GivenWhenThen {

  val BaseUri = s"http://${CustomerAppActor.Host}:${CustomerAppActor.Port}"

}
