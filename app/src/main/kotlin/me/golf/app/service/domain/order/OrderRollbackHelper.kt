package me.golf.app.service.domain.order

import me.golf.core.model.domain.order.OrderState
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.stock.StockRepository
import org.springframework.stereotype.Component

@Component
class OrderRollbackHelper(
    _orderRepository: OrderRepository,
    _stockRepository: StockRepository,
    _ticketRepository: TicketRepository
) {

    init {
        orderRepository = _orderRepository
        stockRepository = _stockRepository
        ticketRepository = _ticketRepository
    }

    companion object {
        private lateinit var orderRepository: OrderRepository
        private lateinit var stockRepository: StockRepository
        private lateinit var ticketRepository: TicketRepository

        fun <R> rollbackOrder(orderId: String, userId: Long, block: () -> R): R {
            return runCatching<R> { block() }
                .onSuccess { return it }
                .onFailure {
                    val order = orderRepository.findByIdAndUserId(orderId, userId)
                    val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })
                    stockRepository.cancelReserve(orderId)

                    val cancelOrder = order.changeState(OrderState.FAILURE)
                    val cancelTickets = tickets.map { it.cancel() }

                    orderRepository.save(cancelOrder)
                    ticketRepository.saveAll(cancelTickets)
                }
                .getOrThrow()
        }
    }
}