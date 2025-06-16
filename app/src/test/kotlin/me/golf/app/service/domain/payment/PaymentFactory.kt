package me.golf.app.service.domain.payment

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.enumerate.PaymentMethod
import me.golf.core.model.domain.payment.PaymentMutator
import me.golf.core.model.domain.payment.enumerate.PaymentStatus
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

object PaymentFactory {

    fun createPayment(
        id: Long? = 1L,
        amount: BigDecimal = BigDecimal(240000),
        paymentMethod: PaymentMethod = PaymentMethod.CASH,
        paymentStatus: PaymentStatus = PaymentStatus.PENDING,
        paymentDate: LocalDateTime = LocalDateTime.now(),
        idempotentKey: String = UUID.randomUUID().toString(),
        userId: Long = 1L,
    ): Payment {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        return fixtureMonkey.giveMeBuilder<PaymentMutator>()
            .set("id", id)
            .set("amount", amount)
            .set("paymentMethod", paymentMethod)
            .set("paymentStatus", paymentStatus)
            .set("paymentDate", paymentDate)
            .set("idempotentKey", idempotentKey)
            .set("userId", userId)
            .sample()
    }
}