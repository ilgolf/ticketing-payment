package me.golf.infra.repository.domain.payment

import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import me.golf.core.repository.domain.payment.PaymentEventRepository
import me.golf.infra.dao.domain.payment.PaymentEventJpaDao
import me.golf.infra.entity.converter.toEntity
import me.golf.infra.entity.converter.toModel
import org.springframework.stereotype.Repository

@Repository
class PaymentEventRepositoryImpl(
    private val paymentEventJpaDao: PaymentEventJpaDao
): PaymentEventRepository {

    override fun save(paymentEvent: PaymentEvent) {
        paymentEventJpaDao.save(paymentEvent.toEntity())
    }

    override fun findByStatusAndType(status: EventStatus, type: PaymentEventType): List<PaymentEvent> {
        return paymentEventJpaDao.findByEventStatusAndEventType(status, type)
            .map { it.toModel() }
    }
}