package me.golf.app.api.payment.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import org.jetbrains.annotations.NotNull
import java.math.BigDecimal

data class PaymentRequest(

    @field:NotBlank
    @field:Pattern(regexp = "^[0-9]{14}-[0-9]{1,4}\$")
    val orderId: String,

    @field:NotNull
    val userId: Long,

    @field:NotNull
    val amount: BigDecimal,

    @field:NotBlank
    val paymentKey: String,
) {
    fun toMessage() = PaymentRequestMessage(
        orderId = orderId,
        userId = userId,
        amount = amount,
        paymentKey = paymentKey,
    )
}