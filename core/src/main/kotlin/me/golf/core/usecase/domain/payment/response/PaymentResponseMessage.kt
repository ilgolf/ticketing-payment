package me.golf.core.usecase.domain.payment.response

import java.time.LocalDateTime

data class PaymentResponseMessage(val paymentId: Long, val paymentDate: LocalDateTime)
