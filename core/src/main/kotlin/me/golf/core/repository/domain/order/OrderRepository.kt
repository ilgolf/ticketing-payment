package me.golf.core.repository.domain.order

import me.golf.core.model.domain.order.Order

interface OrderRepository {

    fun save(order: Order): Order

    fun findByIdAndUserId(orderId: String, userId: Long): Order

    fun findWithPaymentById(id: String): Order

    fun findByPaymentId(paymentIds: List<Long>): List<Order>
}