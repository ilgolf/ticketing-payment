package me.golf.infra.dao.domain.order

import me.golf.infra.entity.domain.order.OrderEntity
import org.springframework.data.jpa.repository.JpaRepository

interface OrderJpaDao: JpaRepository<OrderEntity, String> {
    fun findByIdAndUserId(id: String, userId: Long): OrderEntity?
}