package me.golf.infra.entity.domain.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import java.time.LocalDateTime

@Entity
@Table(name = "payment_event_entity")
class PaymentEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "event_id", nullable = false)
    var eventId: Long? = null,
    @Column(name = "paymentId", nullable = false)
    val paymentId: Long,
    @Column(name = "eventType", nullable = false)
    val eventType: PaymentEventType,
    @Column(name = "eventStatus", nullable = false)
    val eventStatus: EventStatus,
    @Column(name = "occurredAt", nullable = false)
    val occurredAt: LocalDateTime,
    @Column(name = "processedAt", nullable = true)
    val processedAt: LocalDateTime?,
    @Column(name = "errorMessage", nullable = true)
    val errorMessage: String?,
)