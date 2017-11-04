package sk.bsmk.customer

import org.scalatest.Suites

class CustomerAppSuite
    extends Suites(
      new CustomerAppWordSpecSuite,
      new CustomerAppFeatureSpecSuite
    )
    with RunningApp {}
