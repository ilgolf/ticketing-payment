package me.golf.infra.entity.converter

import me.golf.core.model.domain.payment.Payment
import me.golf.infra.entity.domain.payment.PaymentEntity

fun Payment.toEntity(orderId: String) = PaymentEntity(
    id = this.id,
    amount = this.amount,
    paymentMethod = this.paymentMethod,
    paymentStatus = this.paymentStatus,
    paymentDate = this.paymentDate,
    idempotentKey = this.idempotentKey,
    userId = this.userId,
    orderId = orderId,
)

fun PaymentEntity.toModel() = Payment.create(
    id = this.id,
    amount = this.amount,
    paymentMethod = this.paymentMethod,
    paymentStatus = this.paymentStatus,
    paymentDate = this.paymentDate,
    idempotentKey = this.idempotentKey,
    userId = this.userId,
)