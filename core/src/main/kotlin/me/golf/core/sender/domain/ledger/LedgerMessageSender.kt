package me.golf.core.sender.domain.ledger

interface LedgerMessageSender {

    fun send(orderId: String, userId: Long, traceId: String)
}