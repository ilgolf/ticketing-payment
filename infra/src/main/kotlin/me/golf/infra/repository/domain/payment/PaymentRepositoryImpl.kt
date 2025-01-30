package me.golf.infra.repository.domain.payment

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.infra.client.PaymentClient
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.dao.domain.payment.PaymentJpaDao
import me.golf.infra.entity.converter.toEntity
import me.golf.infra.entity.converter.toModel
import me.golf.infra.entity.domain.order.OrderEntity
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val paymentJpaDao: PaymentJpaDao,
    private val orderJpaDao: OrderJpaDao,
    private val paymentClient: PaymentClient
) : PaymentRepository {

    override fun save(payment: Payment, order: Order): Payment {
        val savedPayment = payment.toEntity(order.orderId).let { paymentJpaDao.save(it) }.toModel()
        val orderEntity: OrderEntity = order.addPayment(savedPayment).toEntity()

        orderJpaDao.save(orderEntity)

        return savedPayment
    }

    override fun confirm(payment: Payment, orderId: String): Payment {
        val paymentResult = paymentClient.payment(payment, orderId)
        val paymentEntity = paymentResult.toEntity(payment, orderId)

        return paymentEntity.toModel()
    }

    override fun update(payment: Payment, orderId: String): Payment {
        return paymentJpaDao.save(payment.toEntity(orderId)).toModel()
    }
}