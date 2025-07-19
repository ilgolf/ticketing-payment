package me.golf.core.sender.domain.wallet

interface WalletMessageSender {

    fun send(orderId: String, userId: Long, traceId: String)
}
