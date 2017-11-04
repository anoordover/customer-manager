package sk.bsmk.customer

import org.scalatest.Suites
import sk.bsmk.customer.features.CustomerRegistrationFeature

class CustomerAppFeatureSpecSuite
    extends Suites(
      new CustomerRegistrationFeature
    )
