package me.golf.app.api.payment.response

import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage
import java.time.LocalDateTime

data class PaymentResponse(val paymentId: Long, val paymentDate: LocalDateTime) {

    companion object {
        fun of(message: PaymentResponseMessage): PaymentResponse {
            return PaymentResponse(
                paymentId = message.paymentId,
                paymentDate = message.paymentDate
            )
        }
    }
}