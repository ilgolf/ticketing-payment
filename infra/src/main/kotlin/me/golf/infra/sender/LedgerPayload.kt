package me.golf.infra.sender

data class LedgerPayload(
    val orderId: String,
    val userId: Long
)
