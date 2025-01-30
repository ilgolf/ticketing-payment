package me.golf.core.repository.domain.payment

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment

interface PaymentRepository {

    fun save(payment: Payment, order: Order): Payment

    fun confirm(payment: Payment, orderId: String): Payment

    fun update(payment: Payment, orderId: String): Payment
}