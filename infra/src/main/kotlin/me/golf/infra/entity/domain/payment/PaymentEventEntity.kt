package me.golf.infra.entity.domain.payment

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import java.time.LocalDateTime

@Entity
class PaymentEventEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val eventId: Long?,
    @Column(name = "paymentId", nullable = true)
    val paymentId: Long,
    @Column(name = "eventType", nullable = true)
    val eventType: PaymentEventType,
    @Column(name = "eventStatus", nullable = true)
    val eventStatus: EventStatus,
    @Column(name = "occurredAt", nullable = true)
    val occurredAt: LocalDateTime,
    @Column(name = "processedAt")
    val processedAt: LocalDateTime?,
    @Column(name = "errorMessage")
    val errorMessage: String?,
)