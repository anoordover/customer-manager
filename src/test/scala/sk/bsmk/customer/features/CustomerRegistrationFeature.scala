package sk.bsmk.customer.features

import org.scalatest.DoNotDiscover

@DoNotDiscover
class CustomerRegistrationFeature extends CustomerFeatureSpec {

  feature("Customer registration") {

    scenario("customer with new email") {

      Given("there is no customer with given email")

      When("customer registers with email")

      Then("customer can obtain his detail")

    }

  }

}
