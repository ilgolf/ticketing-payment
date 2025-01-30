package me.golf.core.usecase.domain.order.usecase.message

import java.math.BigDecimal

data class CheckoutCompleteResponseMessage(
    val orderId: String,
    val amount: BigDecimal,
    val idempotentKey: String,
)
