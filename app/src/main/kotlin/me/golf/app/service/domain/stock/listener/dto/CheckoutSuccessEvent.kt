package me.golf.app.service.domain.stock.listener.dto

data class CheckoutSuccessEvent(
    val orderId: String,
    val ticketIds: List<Long>
)
