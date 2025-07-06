package me.golf.core.usecase.domain.order.usecase.message

import me.golf.core.model.domain.order.Order
import java.math.BigDecimal

data class CreateOrderResponseMessage(
    val orderId: String,
    val amount: BigDecimal,
) {

    companion object {
        fun from(order: Order): CreateOrderResponseMessage {
            return CreateOrderResponseMessage(
                orderId = order.orderId,
                amount = order.amount,
            )
        }
    }
}
