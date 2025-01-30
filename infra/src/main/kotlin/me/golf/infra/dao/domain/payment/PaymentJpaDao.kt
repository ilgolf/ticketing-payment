package me.golf.infra.dao.domain.payment

import me.golf.infra.entity.domain.payment.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentJpaDao: JpaRepository<PaymentEntity, Long> {
    fun findByOrderIdAndPaymentStatus(orderId: String, status: String): PaymentEntity?
}