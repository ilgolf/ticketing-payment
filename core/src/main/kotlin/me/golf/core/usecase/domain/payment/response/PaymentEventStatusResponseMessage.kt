package me.golf.core.usecase.domain.payment.response

data class PaymentEventStatusResponseMessage(
    val paymentId: Long,
    val paymentEventId: Long,
)
