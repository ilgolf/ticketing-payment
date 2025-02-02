package me.golf.app.service.domain.order

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
import org.springframework.stereotype.Service
import java.time.LocalDateTime

@Service
class OrderService(
    private val orderRepository: OrderRepository,
    private val ticketRepository: TicketRepository,
    private val stockRepository: StockRepository,
    private val orderIdGenerator: OrderIdGenerator,
) : OrderUseCase {

    override fun order(ticketIds: List<Long>, userId: Long): CreateOrderResponseMessage {
        val tickets: List<Ticket> = ticketRepository.findAllByOrderId(ticketIds)
        val orderId = orderIdGenerator.generateOrderId()

        if (stockRepository.alreadyReserveByTicketIds(ticketIds)) {
            throw IllegalArgumentException("이미 선점중인 상품입니다.")
        }

        val order = Order.create(
            orderId = orderId,
            amount = tickets.sumOf { it.price },
            orderDate = LocalDateTime.now(),
            userId = userId,
            orderState = OrderState.TRY_ORDER,
            payment = null,
            orderItem = tickets.map { OrderItem.create(ticketId = it.id, orderId = orderId) },
        )

        val savedOrder = orderRepository.save(order)

        return CreateOrderResponseMessage(
            savedOrder.orderId,
            savedOrder.amount,
        )
    }
}