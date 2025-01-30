package me.golf.app.api.payment.request

import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import java.math.BigDecimal

data class PaymentRequest(
    val orderId: String,
    val userId: Long,
    val amount: BigDecimal,
    val paymentKey: String,
) {
    fun toMessage() = PaymentRequestMessage(
        orderId = orderId,
        userId = userId,
        amount = amount,
        paymentKey = paymentKey,
    )
}