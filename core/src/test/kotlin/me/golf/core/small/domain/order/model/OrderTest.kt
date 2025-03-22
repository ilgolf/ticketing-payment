package me.golf.core.small.domain.order.model

import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.order.OrderState
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

class OrderTest {

    private lateinit var order: Order

    @BeforeEach
    fun setup() {
        order = Order.create(
            orderId = "202502262233-0001",
            amount = BigDecimal(200000),
            orderDate = LocalDateTime.now(),
            userId = 1L,
            orderState = OrderState.TRY_ORDER,
            payment = null,
            orderItem = listOf(OrderItem.create(1L, "202502262233-0001", 1L)),
        )
    }

    @Test
    @DisplayName("주문 상태를 변경할 수 있습니다.")
    fun test1() {
        // given
        val expected = OrderState.SUCCESS

        // when
        val result = order.changeState(expected)

        // then
        result.orderState shouldBe expected
    }

    @Test
    @DisplayName("결제 시 주문 내역에 결제정보를 추가합니다.")
    fun test2() {
        // given
        val payment = Payment.create(
            amount = BigDecimal(200000),
            paymentMethod = PaymentMethod.CARD,
            paymentStatus = PaymentStatus.PENDING,
            paymentDate = LocalDateTime.now(),
            idempotentKey = UUID.randomUUID().toString(),
            userId = 1L,
        )

        // when
        val result = order.addPayment(payment)

        // then
        result.payment shouldNotBe null
    }
}