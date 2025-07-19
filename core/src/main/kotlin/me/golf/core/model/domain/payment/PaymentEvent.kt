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

    fun changeEventStatus(eventStatus: EventStatus): PaymentEvent

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
    override var eventId: Long?,
    override var paymentId: Long,
    override var eventType: PaymentEventType,
    override var eventStatus: EventStatus,
    override var occurredAt: LocalDateTime,
    override var processedAt: LocalDateTime?,
    override var errorMessage: String?,
) : PaymentEvent {

    override fun changeEventStatus(eventStatus: EventStatus): PaymentEvent {
        return PaymentEventMutator(
            eventId = this.eventId,
            paymentId = this.paymentId,
            eventType = this.eventType,
            eventStatus = eventStatus,
            occurredAt = this.occurredAt,
            processedAt = this.processedAt,
            errorMessage = this.errorMessage
        )
    }
}


