package me.golf.core.usecase.domain.payment.request

import java.math.BigDecimal

data class PaymentRequestMessage(
    val userId: Long,
    val orderId: String,
    val amount: BigDecimal,
    val paymentKey: String,
) {
}