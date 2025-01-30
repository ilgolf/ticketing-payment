package me.golf.infra.entity.converter

import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.infra.entity.domain.payment.PaymentEntity

fun Payment.toEntity(orderId: String) = PaymentEntity(
    id = this.id,
    amount = this.amount,
    paymentMethod = this.paymentMethod.name,
    paymentStatus = this.paymentStatus.name,
    paymentDate = this.paymentDate,
    idempotentKey = this.idempotentKey,
    userId = this.userId,
    orderId = orderId,
)

fun PaymentEntity.toModel() = Payment.create(
    id = this.id,
    amount = this.amount,
    paymentMethod = PaymentMethod.valueOf(this.paymentMethod),
    paymentStatus = PaymentStatus.valueOf(this.paymentStatus),
    paymentDate = this.paymentDate,
    idempotentKey = this.idempotentKey,
    userId = this.userId,
)