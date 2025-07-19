package me.golf.app.service.domain.payment.listener.dto

data class WalletEventMessage(
    val orderId: String,
    val paymentId: Long,
    val userId: Long
)
