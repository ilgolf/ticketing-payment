package me.golf.app.api.order.request

data class CreateOrderRequest(
    val ticketIds: List<Long>,
    val userId: Long,
)
