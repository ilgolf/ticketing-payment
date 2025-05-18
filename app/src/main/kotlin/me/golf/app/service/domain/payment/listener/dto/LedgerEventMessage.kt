package me.golf.app.service.domain.payment.listener.dto

data class LedgerEventMessage(
    val paymentId: Long,
    val userId: Long
)
