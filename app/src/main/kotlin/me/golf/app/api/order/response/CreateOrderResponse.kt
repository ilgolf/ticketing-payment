package me.golf.app.api.order.response

import me.golf.core.usecase.domain.order.usecase.message.CreateOrderResponseMessage
import java.math.BigDecimal

data class CreateOrderResponse(
    val orderId: String,
    val amount: BigDecimal,
) {
    companion object {
        fun from(message: CreateOrderResponseMessage): CreateOrderResponse {
            return CreateOrderResponse(
                message.orderId,
                message.amount,
            )
        }
    }
}
