package me.golf.infra.dao.domain.payment

import me.golf.infra.entity.domain.payment.PaymentEventEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentEventJpaDao: JpaRepository<PaymentEventEntity, Long>
