package me.golf.infra.repository.domain.payment

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.infra.converter.toEntity
import me.golf.infra.converter.toModel
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.dao.domain.payment.PaymentJpaDao
import me.golf.infra.entity.domain.order.OrderEntity
import me.golf.infra.entity.domain.payment.PaymentEntity
import org.springframework.stereotype.Repository

@Repository
class PaymentRepositoryImpl(
    private val paymentJpaDao: PaymentJpaDao,
    private val orderJpaDao: OrderJpaDao,
) : PaymentRepository {

    override fun save(payment: Payment, order: Order): Payment {
        val savedPayment = payment.toEntity().let { paymentJpaDao.save(it) }.toModel()
        val orderEntity: OrderEntity = order.addPayment(savedPayment).toEntity()

        orderJpaDao.save(orderEntity)

        return savedPayment
    }
}