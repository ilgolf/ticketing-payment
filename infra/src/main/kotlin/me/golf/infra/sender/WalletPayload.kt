package me.golf.infra.sender

data class WalletPayload(
    val orderId: String,
    val userId: Long
)