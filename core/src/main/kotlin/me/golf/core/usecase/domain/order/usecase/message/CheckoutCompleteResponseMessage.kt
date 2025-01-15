package me.golf.core.usecase.domain.order.usecase.message

import me.golf.core.model.domain.order.OrderState

data class CheckoutCompleteResponseMessage(
    val orderId: Long,
    val orderStatus: OrderState
)
