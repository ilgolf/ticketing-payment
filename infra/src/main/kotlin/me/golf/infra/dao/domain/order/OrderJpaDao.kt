package me.golf.infra.dao.domain.order

import me.golf.infra.entity.domain.order.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface OrderJpaDao: JpaRepository<OrderEntity, String> {

    fun findByIdAndUserId(id: String, userId: Long): OrderEntity?

    @Query("""
        SELECT o 
        FROM OrderEntity AS o
          JOIN FETCH PaymentEntity AS p ON o.id = p.orderId
        WHERE p.id IN (:paymentIds)
    """)
    fun findByPaymentIdsIn(paymentIds: List<Long>): List<OrderEntity>
}