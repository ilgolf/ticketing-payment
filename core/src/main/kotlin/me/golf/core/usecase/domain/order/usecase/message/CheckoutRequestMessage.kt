package me.golf.core.usecase.domain.order.usecase.message

import me.golf.core.model.domain.payment.PaymentMethod

data class CheckoutRequestMessage(
    val orderId: Long,
    val userId: Long,
    val paymentMethod: PaymentMethod,
)
