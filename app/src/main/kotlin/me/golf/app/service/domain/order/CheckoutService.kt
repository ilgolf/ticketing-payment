package me.golf.app.service.domain.order

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.repository.domain.stock.StockRepository
import me.golf.core.usecase.domain.order.usecase.CheckoutUseCase
import me.golf.core.usecase.domain.order.usecase.message.CheckoutCompleteResponseMessage
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime
import java.util.*

@Service
class CheckoutService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val stockRepository: StockRepository,
    private val paymentRepository: PaymentRepository
) : CheckoutUseCase {

    @Transactional
    override fun checkout(message: CheckoutRequestMessage): CheckoutCompleteResponseMessage {
        val order: Order = orderRepository.findByIdAndUserId(message.orderId, message.userId)
        val tickets: List<Ticket> = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })

        if (tickets.isEmpty()) {
            throw IllegalArgumentException("주문 티켓이 존재하지 않습니다.")
        }

        tickets.filter { it.isNonPurchase() }
            .takeIf { it.isNotEmpty() }
            ?.let { throw IllegalArgumentException("구매할 수 없는 주문 상태입니다. 현재 상태 : ${it.size}") }

        if (stockRepository.alreadyReserve(order.orderId)) {
            throw IllegalArgumentException("이미 선점 중인 상품입니다.")
        }

        // create payment
        val payment = createPayment(order, message.paymentMethod)
        paymentRepository.save(payment, order)
        val reserveStockResult = stockRepository.reserveStock(orderId = order.orderId, tickets.map { it.id })

        if (!reserveStockResult) {
            throw IllegalArgumentException("상품 선점에 실패했습니다.")
        }

        return CheckoutCompleteResponseMessage(
            order.orderId,
            order.orderState
        )
    }

    private fun createPayment(order: Order, paymentMethod: PaymentMethod) =
        Payment.create(
            amount = order.amount,
            userId = order.userId,
            idempotentKey = UUID.randomUUID(),
            paymentMethod = paymentMethod,
            paymentStatus = PaymentStatus.PENDING,
            paymentDate = LocalDateTime.now(),
        )
}
