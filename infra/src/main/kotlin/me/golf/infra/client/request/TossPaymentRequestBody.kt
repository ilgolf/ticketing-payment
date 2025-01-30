package me.golf.infra.client.request

import java.math.BigDecimal

data class TossPaymentRequestBody(
    val paymentKey: String,
    val amount: BigDecimal,
    val orderId: String
)
