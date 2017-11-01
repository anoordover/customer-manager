package sk.bsmk.customer

final case class Customer(
    email: String,
    voucherIds: Set[String] = Set.empty
) {

  def addVoucher(voucherId: String): Customer = {
    this.copy(voucherIds = voucherIds + voucherId)
  }

  def removeVoucher(voucherId: String): Customer = {
    this.copy(voucherIds = voucherIds - voucherId)
  }

}
