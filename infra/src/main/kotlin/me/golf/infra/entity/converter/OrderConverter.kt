package me.golf.infra.entity.converter

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.order.OrderState
import me.golf.core.model.domain.payment.Payment
import me.golf.infra.entity.domain.order.OrderEntity
import me.golf.infra.entity.domain.order.OrderItemEntity

fun Order.toEntity(): OrderEntity {
    return OrderEntity(
        id = this.orderId,
        amount = this.amount,
        orderDate = this.orderDate,
        orderState = this.orderState.name,
        userId = this.userId,
    )
}

fun OrderEntity.toModel(payment: Payment? = null, orderItems: List<OrderItem>): Order =
    Order.create(
        orderId = this.id,
        amount = this.amount,
        orderDate = this.orderDate,
        userId = this.userId,
        orderState = OrderState.valueOf(this.orderState),
        payment = payment,
        orderItem = orderItems,
    )

fun OrderItem.toEntity(orderId: String? = null) = OrderItemEntity(
    id = this.orderItemId,
    ticketId = this.itemId,
    orderId = orderId?: this.orderId,
)

fun OrderItemEntity.toModel() = OrderItem.create(
    id = this.id!!,
    ticketId = this.ticketId,
    orderId = this.orderId,
)