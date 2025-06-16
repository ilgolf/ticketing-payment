package me.golf.infra.repository.domain.payment

import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.repository.domain.payment.PaymentEventRepository
import me.golf.infra.dao.domain.payment.PaymentEventJpaDao
import org.springframework.stereotype.Repository

@Repository
class PaymentEventRepositoryImpl(
    private val paymentEventJpaDao: PaymentEventJpaDao
): PaymentEventRepository {

    override fun save(paymentEvent: PaymentEvent) {
        paymentEventJpaDao.save(paymentEvent)
    }
}