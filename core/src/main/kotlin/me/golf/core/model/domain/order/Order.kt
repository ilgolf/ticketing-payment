package me.golf.core.model.domain.order

import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.ticket.Ticket
import java.math.BigDecimal
import java.time.LocalDateTime

interface Order {

    /**
     * 주문 식별자
     */
    val orderId: String

    /**
     * 주문 총 가격
     */
    val amount: BigDecimal

    /**
     * 주문 날짜
     */
    val orderDate: LocalDateTime

    /**
     * 주문 상태
     */
    val orderState: OrderState

    /**
     * 결제 정보 - 결제 전에는 존재하지 않습니다.
     */
    val payment: Payment?

    /**
     * 구매자 ID
     */
    val userId: Long

    /**
     * 주문 티켓 모음
     */
    val orderItem: List<OrderItem>

    fun changeState(state: OrderState): Order
    fun addPayment(payment: Payment): Order

    companion object {
        fun create(
            orderId: String,
            amount: BigDecimal,
            orderDate: LocalDateTime,
            userId: Long,
            orderState: OrderState,
            payment: Payment?,
            orderItem: List<OrderItem>,
        ): Order {
            return OrderMutator(
                orderId,
                amount,
                orderDate,
                userId,
                orderState,
                payment,
                orderItem,
            )
        }

        fun order(orderId: String, userId: Long, tickets: List<Ticket>): Order {
            return create(
                orderId = orderId,
                amount = tickets.sumOf { it.price },
                orderDate = LocalDateTime.now(),
                userId = userId,
                orderState = OrderState.TRY_ORDER,
                payment = null,
                orderItem = tickets.map { OrderItem.create(ticketId = it.id, orderId = orderId) },
            )
        }
    }
}

class OrderMutator(
    override val orderId: String,
    override val amount: BigDecimal,
    override val orderDate: LocalDateTime,
    override val userId: Long,
    override val orderState: OrderState,
    override val payment: Payment?,
    override val orderItem: List<OrderItem>,
) : Order {

    override fun changeState(state: OrderState): Order {
        return OrderMutator(
            orderId = this.orderId,
            amount = this.amount,
            orderDate = this.orderDate,
            userId = this.userId,
            orderState = state,
            payment = this.payment,
            orderItem = this.orderItem,
        )
    }

    override fun addPayment(payment: Payment): Order {
        return OrderMutator(
            orderId = this.orderId,
            amount = this.amount,
            orderDate = this.orderDate,
            userId = this.userId,
            orderState = this.orderState,
            payment = payment,
            orderItem = this.orderItem,
        )
    }
}
