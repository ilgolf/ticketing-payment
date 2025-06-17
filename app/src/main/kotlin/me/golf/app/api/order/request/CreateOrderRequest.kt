package me.golf.app.api.order.request

import jakarta.validation.constraints.NotNull

data class CreateOrderRequest(

    @field:NotNull
    val ticketIds: List<Long>,

    @field:NotNull
    val userId: Long,
)
