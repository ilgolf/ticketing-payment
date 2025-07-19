package me.golf.core.usecase.domain.payment.request

import me.golf.core.model.domain.payment.enumerate.EventStatus

data class UpdatePaymentEventStatusRequestMessage(
    val userId: Long,
    val orderId: String,
    val eventStatus: EventStatus
)
