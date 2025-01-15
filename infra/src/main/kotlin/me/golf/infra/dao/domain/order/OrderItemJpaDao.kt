package me.golf.infra.dao.domain.order

import me.golf.infra.entity.domain.order.OrderItemEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface OrderItemJpaDao: JpaRepository<OrderItemEntity, Long> {
    fun findAllByOrderId(orderId: Long): MutableList<OrderItemEntity>
}