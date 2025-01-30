package me.golf.infra.client.response

data class TossPaymentErrorResponse(
    val code: PaymentErrorType,
    val message: String,
)
