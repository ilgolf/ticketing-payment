package me.golf.infra.sender

data class WalletPayload(
    val paymentId: Long,
    val userId: Long
)