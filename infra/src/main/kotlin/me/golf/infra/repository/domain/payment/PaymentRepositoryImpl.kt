package me.golf.infra.repository.domain.payment

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.infra.client.PaymentClient
import me.golf.infra.client.response.PaymentResponse
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.dao.domain.payment.PaymentEventJpaDao
import me.golf.infra.dao.domain.payment.PaymentJpaDao
import me.golf.infra.entity.converter.toEntity
import me.golf.infra.entity.converter.toModel
import me.golf.infra.entity.domain.order.OrderEntity
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val paymentJpaDao: PaymentJpaDao,
    private val paymentEventJpaDao: PaymentEventJpaDao,
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
        val paymentResult: PaymentResponse = paymentClient.payment(payment, orderId)
        val paymentMethod = paymentResult.getPaymentMethod()

        return payment.payment(paymentResult.approvedAt, paymentMethod)
    }

    override fun update(payment: Payment, orderId: String): Payment {
        return paymentJpaDao.save(payment.toEntity(orderId)).toModel()
    }

    override fun findEventByOrderId(orderId: String, userId: Long): PaymentEvent {
        val paymentEntity = paymentJpaDao.findByOrderIdAndUserId(orderId, userId)
            ?: throw IllegalArgumentException("Payment with id $orderId not found")

        val paymentEventEntity = (paymentEventJpaDao.findByIdOrNull(paymentEntity.id)
            ?: throw IllegalArgumentException("payment Event wwith id ${paymentEntity.id} not found"))

        return paymentEventEntity.toModel()
    }

    override fun updateEvent(updatedPaymentEvent: PaymentEvent) {
        paymentEventJpaDao.save(updatedPaymentEvent.toEntity())
    }
}