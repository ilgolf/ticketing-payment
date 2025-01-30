package me.golf.core.usecase.domain.payment

import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage

interface PaymentUseCase {

    fun payment(message: PaymentRequestMessage): PaymentResponseMessage
}