package me.golf.core.sender.domain.wallet

interface WalletMessageSender {

    fun send(paymentId: Long, userId: Long)
}
