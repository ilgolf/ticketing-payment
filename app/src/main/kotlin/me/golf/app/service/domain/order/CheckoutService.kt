package me.golf.app.service.domain.order

import me.golf.app.service.domain.stock.listener.dto.CheckoutSuccessEvent
import me.golf.app.service.domain.stock.listener.dto.OrderFailEvent
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.enumerate.PaymentMethod
import me.golf.core.model.domain.payment.enumerate.PaymentStatus
import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.repository.domain.stock.StockRepository
import me.golf.core.usecase.domain.order.usecase.CheckoutUseCase
import me.golf.core.usecase.domain.order.usecase.message.CheckoutCompleteResponseMessage
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class CheckoutService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val stockRepository: StockRepository,
    private val paymentRepository: PaymentRepository,
    private val eventPublisher: ApplicationEventPublisher
) : CheckoutUseCase {

    @Transactional
    override fun checkout(message: CheckoutRequestMessage): CheckoutCompleteResponseMessage {
        val order = orderRepository.findByIdAndUserId(message.orderId, message.userId)
        val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })

        kotlin.runCatching { validateTickets(tickets, message) }
            .onFailure { publishFailureEventAndThrow(message, it) }

        eventPublisher.publishEvent(CheckoutSuccessEvent(order.orderId, tickets.map { it.id }))
        
        val payment = order.payment ?: createAndSaveNewPayment(order, message.paymentMethod)
        
        return CheckoutCompleteResponseMessage(order.orderId, order.amount, payment.idempotentKey)
    }

    private fun validateTickets(tickets: List<Ticket>, message: CheckoutRequestMessage) {
        validateTicketsExist(tickets)
        validateTicketsPurchasable(tickets)
        validateTicketsNotAlreadyReserved(tickets.map { it.id }, message.orderId)
    }

    private fun validateTicketsExist(tickets: List<Ticket>) {
        if (tickets.isEmpty()) {
            throw IllegalArgumentException("주문 티켓이 존재하지 않습니다.")
        }
    }

    private fun validateTicketsPurchasable(tickets: List<Ticket>) {
        if (tickets.any { it.isNonPurchase() }) {
            throw IllegalArgumentException("구매할 수 없는 주문 상태입니다.")
        }
    }

    private fun validateTicketsNotAlreadyReserved(ticketIds: List<Long>, orderId: String) {
        if (stockRepository.alreadyReserveByTicketIds(orderId, ticketIds)) {
            throw IllegalArgumentException("이미 선점 중인 상품입니다.")
        }
    }

    private fun publishFailureEventAndThrow(message: CheckoutRequestMessage, exception: Throwable): Nothing {
        eventPublisher.publishEvent(OrderFailEvent(message.orderId))
        throw exception
    }

    private fun createAndSaveNewPayment(order: Order, paymentMethod: PaymentMethod): Payment {
        val payment = order.createPayment(paymentMethod)
        return paymentRepository.save(payment, order)
    }

    private fun Order.createPayment(paymentMethod: PaymentMethod): Payment {
        return Payment.create(
            amount = this.amount,
            userId = this.userId,
            idempotentKey = "",
            paymentMethod = paymentMethod,
            paymentStatus = PaymentStatus.PENDING,
            paymentDate = LocalDateTime.now(),
        )
    }
}
