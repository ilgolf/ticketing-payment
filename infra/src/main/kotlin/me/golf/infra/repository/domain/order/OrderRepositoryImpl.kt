package me.golf.infra.repository.domain.order

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.infra.dao.domain.order.OrderItemJpaDao
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.dao.domain.payment.PaymentJpaDao
import me.golf.infra.entity.converter.toEntity
import me.golf.infra.entity.converter.toModel
import me.golf.infra.entity.domain.order.OrderItemEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class OrderRepositoryImpl(
    private val orderJpaDao: OrderJpaDao,
    private val orderItemJpaDao: OrderItemJpaDao,
    private val paymentJpaDao: PaymentJpaDao
) : OrderRepository {

    override fun save(order: Order): Order {
        val savedOrder = orderJpaDao.save(order.toEntity())
        val orderItemEntities: List<OrderItemEntity> = orderItemJpaDao.saveAll(order.orderItem.map { it.toEntity(savedOrder.id) })
        return savedOrder.toModel(orderItems = orderItemEntities.map { it.toModel() })
    }

    override fun findByIdAndUserId(orderId: String, userId: Long): Order {
        val order = orderJpaDao.findByIdAndUserId(orderId, userId)
            ?: throw IllegalArgumentException("Order with id=$orderId does not exist")

        val orderItems = orderItemJpaDao.findAllByOrderId(orderId)
        val payment = paymentJpaDao.findByOrderIdAndPaymentStatus(order.id, PaymentStatus.PENDING.name)

        return order.toModel(payment = payment?.toModel(), orderItems = orderItems.map { it.toModel() })
    }

    override fun findWithPaymentById(id: String): Order? {
        val orderEntity = orderJpaDao.findByIdOrNull(id)
            ?: throw IllegalArgumentException("Order with id=$id does not exist")

        val orderItems = orderItemJpaDao.findAllByOrderId(id)
        val paymentEntity = paymentJpaDao.findByOrderIdAndPaymentStatus(orderEntity.id, PaymentStatus.PENDING.name)
            ?: throw IllegalArgumentException("Payment with id=$id does not exist")

        val payment = paymentEntity.toModel()

        return orderEntity.toModel(payment = payment, orderItems = orderItems.map { it.toModel() })
    }
}
