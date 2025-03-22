package me.golf.core.small.domain.payment

import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

object PaymentFactory {

    fun createPayment(
        id: Long? = 1L,
        amount: BigDecimal? = BigDecimal(240000),
        paymentMethod: PaymentMethod? = PaymentMethod.CASH,
        paymentStatus: PaymentStatus? = PaymentStatus.PENDING,
        paymentDate: LocalDateTime? = LocalDateTime.now(),
        idempotentKey: String? = UUID.randomUUID().toString(),
        userId: Long? = 1L,
    ): Payment {
        return Payment.create(
            id = id ?: 1L,
            amount = amount ?: BigDecimal(240000),
            paymentMethod = paymentMethod ?: PaymentMethod.CASH,
            paymentStatus = paymentStatus ?: PaymentStatus.PENDING,
            paymentDate = paymentDate ?: LocalDateTime.now(),
            idempotentKey = idempotentKey ?: UUID.randomUUID().toString(),
            userId = userId ?: 1L,
        )
    }
}