package me.golf.infra.dao.domain.payment

import me.golf.core.model.domain.payment.enumerate.PaymentStatus
import me.golf.infra.entity.domain.payment.PaymentEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param

interface PaymentJpaDao: JpaRepository<PaymentEntity, Long> {

    @Query("""
        SELECT p
        FROM PaymentEntity p
        WHERE p.orderId = :orderId
          AND p.paymentStatus = :paymentStatus
    """)
    fun findByOrderIdAndPaymentStatus(
        @Param("orderId") orderId: String,
        @Param("paymentStatus") status: PaymentStatus
    ): PaymentEntity?

    fun findByOrderIdAndUserId(orderId: String, userId: Long): PaymentEntity?
}