package me.golf.infra.entity.converter

import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.infra.entity.domain.payment.PaymentEventEntity

fun PaymentEvent.toEntity() = PaymentEventEntity(
    eventId = this.eventId,
    paymentId = this.paymentId,
    eventType = this.eventType,
    eventStatus = this.eventStatus,
    occurredAt = this.occurredAt,
    processedAt = this.processedAt,
    errorMessage = this.errorMessage,
)