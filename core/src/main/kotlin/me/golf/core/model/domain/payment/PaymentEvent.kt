package me.golf.core.model.domain.payment

import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import java.time.LocalDateTime

interface PaymentEvent {
    val eventId: Long?
    val paymentId: Long
    val eventType: PaymentEventType
    val eventStatus: EventStatus
    val occurredAt: LocalDateTime
    val processedAt: LocalDateTime?
    val errorMessage: String?

    companion object {
        fun create(
            eventId: Long? = null,
            paymentId: Long,
            eventType: PaymentEventType,
            eventStatus: EventStatus = EventStatus.PENDING,
            occurredAt: LocalDateTime = LocalDateTime.now(),
            processedAt: LocalDateTime? = null,
            errorMessage: String? = null,
        ): PaymentEventMutator {
            return PaymentEventMutator(
                eventId = eventId,
                paymentId = paymentId,
                eventType = eventType,
                eventStatus = eventStatus,
                occurredAt = occurredAt,
                processedAt = processedAt,
                errorMessage = errorMessage
            )
        }
    }
}

class PaymentEventMutator(
    override val eventId: Long?,
    override val paymentId: Long,
    override val eventType: PaymentEventType,
    override val eventStatus: EventStatus,
    override val occurredAt: LocalDateTime,
    override val processedAt: LocalDateTime?,
    override val errorMessage: String?,
) : PaymentEvent


