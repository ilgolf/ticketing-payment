package me.golf.app.service.domain.order

import me.golf.app.service.domain.stock.listener.dto.OrderCompleteEvent
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.order.OrderState
import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.stock.StockRepository
import me.golf.core.usecase.domain.order.usecase.OrderUseCase
import me.golf.core.usecase.domain.order.usecase.message.CreateOrderResponseMessage
import me.golf.infra.generator.OrderIdGenerator
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val stockRepository: StockRepository,
    private val orderIdGenerator: OrderIdGenerator,
    private val eventPublisher: ApplicationEventPublisher
) : OrderUseCase {

    @Transactional
    override fun order(ticketIds: List<Long>, userId: Long): CreateOrderResponseMessage {
        val tickets: List<Ticket> = ticketRepository.findAllByOrderId(ticketIds)

        validateItemStatus(tickets, ticketIds)

        val orderId = orderIdGenerator.generateOrderId()
        val order = Order.order(orderId, userId, tickets)

        // 선점 이벤트 시작
        eventPublisher.publishEvent(OrderCompleteEvent(orderId, ticketIds))

        return CreateOrderResponseMessage.from(orderRepository.save(order))
    }

    private fun validateItemStatus(tickets: List<Ticket>, ticketIds: List<Long>) {
        if (tickets.size != ticketIds.size) {
            throw IllegalArgumentException("주문 상품이 올바르지 않습니다.")
        }

        if (stockRepository.alreadyReserveByTicketIds(ticketIds)) {
            throw IllegalArgumentException("이미 선점중인 상품입니다.")
        }

        if (tickets.any { it.isNonPurchase() }) {
            throw IllegalArgumentException("이미 구입 된 상품입니다.")
        }
    }
}
