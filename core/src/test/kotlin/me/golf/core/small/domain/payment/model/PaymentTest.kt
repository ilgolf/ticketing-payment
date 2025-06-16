package me.golf.core.small.domain.payment.model

import io.kotest.matchers.shouldBe
import me.golf.core.model.domain.payment.enumerate.PaymentMethod
import me.golf.core.model.domain.payment.enumerate.PaymentStatus
import me.golf.core.small.domain.payment.PaymentFactory
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDateTime
import java.util.UUID

class PaymentTest {

    @Test
    @DisplayName("결제 전 멱등키를 추가합니다.")
    fun test1() {
        // given
        val givenPayment = PaymentFactory.createPayment(idempotentKey = "")
        val expected = UUID.randomUUID().toString()

        // when
        val result = givenPayment.addIdempotentKey(expected)

        // then
        result.idempotentKey shouldBe expected
    }

    @Test
    @DisplayName("결제를 할 수 있습니다.")
    fun test2() {
        // given
        val givenPayment = PaymentFactory.createPayment(paymentStatus = PaymentStatus.PENDING, paymentDate = null)
        val expectedPaymentDate = LocalDateTime.now()
        val expectedPaymentMethod = PaymentMethod.EASY_PAY
        val expectedPaymentStatus = PaymentStatus.COMPLETE

        // when
        val result = givenPayment.payment(expectedPaymentDate, PaymentMethod.EASY_PAY)

        // then
        result.paymentDate shouldBe expectedPaymentDate
        result.paymentMethod shouldBe expectedPaymentMethod
        result.paymentStatus shouldBe expectedPaymentStatus
    }
}