package me.golf.core.repository.domain.payment

import me.golf.core.model.domain.payment.PaymentEvent

interface PaymentEventRepository {

    fun save(paymentEvent: PaymentEvent)
}
