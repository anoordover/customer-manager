package sk.bsmk.customer.features

import io.restassured.RestAssured
import io.restassured.module.scala.RestAssuredSupport.AddThenToResponse
import org.apache.http.HttpStatus
import org.scalatest.DoNotDiscover

@DoNotDiscover
class CustomerRegistrationFeature extends CustomerFeatureSpec {

  feature("Customer registration") {

    scenario("customer with new email") {

      val email = "some@email.com"

      Given("there is no customer with given email")
      RestAssured
        .given()
        .baseUri(BaseUri)
        .when()
        .get(s"$BaseUri/customers/$email")
        .Then()
        .statusCode(HttpStatus.SC_NOT_FOUND)

      When("customer registers with email")

      Then("customer can obtain his detail")

    }

  }

}
