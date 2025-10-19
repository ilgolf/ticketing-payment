package me.golf.infra.dao.domain.payment

import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import me.golf.infra.entity.domain.payment.PaymentEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.List

interface PaymentEventJpaDao: JpaRepository<PaymentEventEntity, Long> {
    fun findByPaymentId(paymentId: Long): PaymentEventEntity?
    fun findByEventStatusAndEventType(eventStatus: EventStatus, eventType: PaymentEventType): List<PaymentEventEntity>
}
