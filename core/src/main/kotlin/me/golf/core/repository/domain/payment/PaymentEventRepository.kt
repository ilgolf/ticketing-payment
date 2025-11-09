package me.golf.core.repository.domain.payment

import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType

interface PaymentEventRepository {

    fun save(paymentEvent: PaymentEvent)
    fun findByStatusAndType(status: EventStatus, type: PaymentEventType): List<PaymentEvent>
    fun saveAll(changeEventStatus: List<PaymentEvent>)
}
