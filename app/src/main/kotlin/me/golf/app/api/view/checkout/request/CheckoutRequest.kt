package me.golf.app.api.view.checkout.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Pattern
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage

data class CheckoutRequest(

    @field:NotBlank
    @field:Pattern(regexp = "^[0-9]{14}-[0-9]{1,4}\$")
    val orderId: String,

    @field:NotNull
    val userId: Long,

    @field:NotNull
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