package me.golf.infra.dao.domain.payment

import me.golf.core.model.domain.payment.PaymentEvent
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEventJpaDao: JpaRepository<PaymentEvent, Long> {

}
