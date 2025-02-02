package me.golf.core.usecase.domain.order.usecase.message

import java.math.BigDecimal

data class CreateOrderResponseMessage(
    val orderId: String,
    val amount: BigDecimal,
)
