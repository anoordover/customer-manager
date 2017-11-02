package sk.bsmk.customer

import org.scalatest.Suites
import sk.bsmk.customer.features.{ApiInfoFeatureSpec, CustomerListFeatureSpec, CustomerRegistrationFeatureSpec}

class CustomerAppFeaturesSuite
    extends Suites(
      new ApiInfoFeatureSpec,
      new CustomerListFeatureSpec,
      new CustomerRegistrationFeatureSpec
    )
    with RunningApp {}
