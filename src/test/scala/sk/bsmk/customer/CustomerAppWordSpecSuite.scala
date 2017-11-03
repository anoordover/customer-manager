package sk.bsmk.customer

import org.scalatest.Suites
import sk.bsmk.customer.wordspec.{ApiInfoWordSpec, CustomerListWordSpec, CustomerRegistrationWordSpec}

class CustomerAppWordSpecSuite
    extends Suites(
      new ApiInfoWordSpec,
      new CustomerListWordSpec,
      new CustomerRegistrationWordSpec
    )
    with RunningApp {}
