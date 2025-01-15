package me.golf.infra.dao.domain.order

import me.golf.infra.entity.domain.order.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaDao: JpaRepository<OrderEntity, Long> {
    fun findByIdAndUserId(id: Long, userId: Long): OrderEntity?
}