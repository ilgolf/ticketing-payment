package me.golf.app.service.domain.order

import io.kotest.matchers.shouldBe
import io.kotest.matchers.throwable.shouldHaveMessage
import me.golf.app.service.domain.ticket.TicketFactory
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.stock.StockRepository
import me.golf.infra.generator.OrderIdGenerator
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mockito.anyList
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.springframework.context.ApplicationEventPublisher

class OrderServiceTest {

    private lateinit var sut: OrderService
    private lateinit var orderRepository: OrderRepository
    private lateinit var ticketRepository: TicketRepository
    private lateinit var stockRepository: StockRepository
    private lateinit var orderIdGenerator: OrderIdGenerator
    private lateinit var eventPublisher: ApplicationEventPublisher
    private lateinit var orderId: String

    @BeforeEach
    fun setup() {
        orderRepository = mock()
        ticketRepository = mock()
        stockRepository = mock()
        orderIdGenerator = mock()
        eventPublisher = mock()
        sut = OrderService(orderRepository, ticketRepository, stockRepository, orderIdGenerator, eventPublisher)
        orderId = "20250223-0001"
    }

    @Test
    @DisplayName("선점이 안되어있거나 이미 팔린 티켓이 아니라면 티켓을 구매할 수 있습니다.")
    fun test1() {
        // given
        val tickets = listOf(TicketFactory.createTicket(id = 1, status = TicketStatus.AVAILABLE))
        val orderItem = tickets.map { OrderItem.create(ticketId = it.id, orderId = orderId) }.first()
        val order = OrderFactory.createOrder(orderItem = orderItem, amount = tickets.sumOf { it.price })

        `when`(ticketRepository.findAllByOrderId(anyList())).thenReturn(tickets)
        `when`(orderIdGenerator.generateOrderId()).thenReturn(orderId)
        `when`(orderRepository.save(any())).thenReturn(order)

        // when
        val result = sut.order(listOf(1L), 1L)

        // then
        assertAll(
            { result.amount shouldBe tickets.sumOf { it.price } },
            { result.orderId shouldBe orderId }
        )
    }

    @Test
    @DisplayName("존재하지 않는 티켓으로 구매를 하려고 시도하면 실패합니다.")
    fun test2() {
        // given
        `when`(ticketRepository.findAllByOrderId(anyList())).thenReturn(emptyList())

        // when
        val exception = catchThrowable { sut.order(listOf(1L), 1L) }

        // then
        exception shouldBe IllegalArgumentException("주문 상품이 올바르지 않습니다.")
    }

    @Test
    @DisplayName("이미 선점 중인 상품은 구매할 수 없습니다.")
    fun test3() {
        // given
        val tickets = listOf(TicketFactory.createTicket(id = 1, status = TicketStatus.AVAILABLE))

        `when`(ticketRepository.findAllByOrderId(anyList())).thenReturn(tickets)
        `when`(stockRepository.alreadyReserveByTicketIds(anyList())).thenReturn(true)

        // when
        val exception = catchThrowable { sut.order(listOf(1L), 1L) }

        // then
        exception shouldBe IllegalArgumentException("이미 선점중인 상품입니다.")
    }

    @Test
    @DisplayName("이미 구매 된 상품이라면 구매할 수 없습니다.")
    fun test4() {
        // given
        val tickets = listOf(TicketFactory.createTicket(id = 1, status = TicketStatus.SOLD))

        `when`(ticketRepository.findAllByOrderId(anyList())).thenReturn(tickets)
        `when`(stockRepository.alreadyReserveByTicketIds(anyList())).thenReturn(false)

        // when
        val exception = catchThrowable { sut.order(listOf(1L), 1L) }

        // then
        exception shouldBe IllegalArgumentException("이미 구입 된 상품입니다.")
    }
}