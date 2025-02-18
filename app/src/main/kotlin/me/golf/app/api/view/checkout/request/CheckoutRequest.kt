package me.golf.app.api.view.checkout.request

import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage

data class CheckoutRequest(
    val orderId: String,
    val userId: Long,
    val paymentMethod: PaymentMethodDto
) {

    fun toMessage(): CheckoutRequestMessage {
        return CheckoutRequestMessage(orderId, userId, PaymentMethod.valueOf(paymentMethod.name))
    }
}

enum class PaymentMethodDto {
    CARD,
    CASH,
    POINT,
}