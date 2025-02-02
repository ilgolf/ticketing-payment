package me.golf.core.usecase.domain.order.usecase

import me.golf.core.usecase.domain.order.usecase.message.CreateOrderResponseMessage

interface OrderUseCase {

    fun order(ticketIds: List<Long>, userId: Long): CreateOrderResponseMessage
}
