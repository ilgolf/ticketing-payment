package me.golf.infra.dao.domain.payment

import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.infra.entity.domain.payment.PaymentEventEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query

interface PaymentEventJpaDao: JpaRepository<PaymentEventEntity, Long> {
    @Modifying
    @Query("update PaymentEventEntity p set p.eventStatus = :eventStatus where p.paymentId = :paymentId")
    fun updateByPaymentId(paymentId: Long, eventStatus: EventStatus)
}
