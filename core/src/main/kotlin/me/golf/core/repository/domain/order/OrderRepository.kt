package me.golf.core.repository.domain.order

import me.golf.core.model.domain.order.Order

interface OrderRepository {

    fun save(order: Order): Order

    fun findByIdAndUserId(orderId: Long, userId: Long): Order
}