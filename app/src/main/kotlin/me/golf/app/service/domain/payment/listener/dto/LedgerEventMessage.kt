package me.golf.app.service.domain.payment.listener.dto

data class LedgerEventMessage(
    val orderId: String,
    val paymentId: Long,
    val userId: Long
)
