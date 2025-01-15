package me.golf.infra.repository.domain.order

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.order.OrderState
import me.golf.core.model.domain.payment.Payment
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.infra.converter.toEntity
import me.golf.infra.converter.toModel
import me.golf.infra.dao.domain.order.OrderItemJpaDao
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.entity.domain.order.OrderEntity
import me.golf.infra.entity.domain.order.OrderItemEntity
import org.springframework.data.jpa.domain.AbstractPersistable_.id
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderJpaDao: OrderJpaDao,
    private val orderItemJpaDao: OrderItemJpaDao
) : OrderRepository {

    override fun save(order: Order): Order {
        val orderItemEntities: List<OrderItemEntity> = orderItemJpaDao.saveAll(order.orderItem.map { it.toEntity() })
        return orderJpaDao.save(order.toEntity()).toModel(orderItems = orderItemEntities.map { it.toModel() })
    }

    override fun findByIdAndUserId(orderId: Long, userId: Long): Order {
        val order = orderJpaDao.findByIdAndUserId(orderId, userId)
            ?: throw IllegalArgumentException("Order with id=$orderId does not exist")

        val orderItems = orderItemJpaDao.findAllByOrderId(orderId)

        return order.toModel(orderItems = orderItems.map { it.toModel() })
    }
}