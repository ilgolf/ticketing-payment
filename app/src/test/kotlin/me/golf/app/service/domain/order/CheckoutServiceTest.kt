package me.golf.app.service.domain.order

import io.kotest.matchers.shouldBe
import me.golf.app.service.domain.payment.PaymentFactory
import me.golf.app.service.domain.ticket.TicketFactory
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentMutator
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.repository.domain.stock.StockRepository
import me.golf.core.usecase.domain.order.usecase.CheckoutUseCase
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.catchThrowable
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertAll
import org.mockito.ArgumentMatchers.anyList
import org.mockito.ArgumentMatchers.anyLong
import org.mockito.Mockito.`when`
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify

@DisplayName("주문 확인 후 : ")
class CheckoutServiceTest {

    private lateinit var sut: CheckoutUseCase
    private lateinit var orderRepository: OrderRepository
    private lateinit var ticketRepository: TicketRepository
    private lateinit var stockRepository: StockRepository
    private lateinit var paymentRepository: PaymentRepository
    private lateinit var order: Order

    @BeforeEach
    fun setUp() {
        orderRepository = mock<OrderRepository>()
        ticketRepository = mock<TicketRepository>()
        stockRepository = mock<StockRepository>()
        paymentRepository = mock<PaymentRepository>()
        sut = CheckoutService(orderRepository, ticketRepository, stockRepository, paymentRepository)

        order = OrderFactory.createOrder()
    }

    @Test
    @DisplayName("정상적인 주문이라면 결제 정보를 생성 후 상품을 선점 합니다.")
    fun test1() {
        // given
        val requestMessage = CheckoutRequestMessage(orderId = 1, userId = 1, PaymentMethod.CASH)
        val ticket = TicketFactory.createTicket(status = TicketStatus.AVAILABLE)
        val payment = PaymentFactory.createPayment()

        `when`(orderRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(order)
        `when`(ticketRepository.findAllByOrderId(anyLong())).thenReturn(listOf(ticket))
        `when`(stockRepository.alreadyReserve(anyList())).thenReturn(false)
        `when`(stockRepository.reserveStock(anyLong(), anyList())).thenReturn(true)
        `when`(paymentRepository.save(any<PaymentMutator>())).thenReturn(payment)

        // when
        val result = sut.checkout(requestMessage)

        // then
        assertAll(
            { result.orderId shouldBe order.orderId },
            { result.orderStatus shouldBe order.orderState },
            { verify(stockRepository).reserveStock(anyLong(), anyList()) },
            { verify(paymentRepository).save(any<PaymentMutator>()) }
        )
    }

    @Test
    @DisplayName("이미 선점 되어있으면 주문을 선점할 수 없습니다.")
    fun test2() {
        // given
        val requestMessage = CheckoutRequestMessage(orderId = 1, userId = 1, PaymentMethod.CASH)
        val ticket = TicketFactory.createTicket(status = TicketStatus.AVAILABLE)

        `when`(orderRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(order)
        `when`(ticketRepository.findAllByOrderId(anyLong())).thenReturn(listOf(ticket))
        `when`(stockRepository.alreadyReserve(anyList())).thenReturn(true)
        `when`(stockRepository.reserveStock(anyLong(), anyList())).thenReturn(true)

        // when
        val exception: Throwable = catchThrowable { sut.checkout(requestMessage) }

        // then
        assertAll(
            { assertThat(exception).isExactlyInstanceOf(IllegalArgumentException::class.java) },
        )
    }

    @Test
    @DisplayName("이미 판매된 티켓은 구매할 수 없습니다.")
    fun test3() {
        // given
        val requestMessage = CheckoutRequestMessage(orderId = 1, userId = 1, PaymentMethod.CASH)
        val soldTicket = TicketFactory.createTicket(status = TicketStatus.SOLD)

        `when`(orderRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(order)
        `when`(ticketRepository.findAllByOrderId(anyLong())).thenReturn(listOf(soldTicket))

        // when
        val exception: Throwable = catchThrowable { sut.checkout(requestMessage) }

        // then
        assertThat(exception).isExactlyInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("비활성화 된 티켓은 구매할 수 없습니다.")
    fun test4() {
        // given
        val requestMessage = CheckoutRequestMessage(orderId = 1, userId = 1, PaymentMethod.CASH)
        val disabledTicket = TicketFactory.createTicket(status = TicketStatus.DISABLED)

        `when`(orderRepository.findByIdAndUserId(anyLong(), anyLong())).thenReturn(order)
        `when`(ticketRepository.findAllByOrderId(anyLong())).thenReturn(listOf(disabledTicket))

        // when
        val exception: Throwable = catchThrowable { sut.checkout(requestMessage) }

        // then
        assertThat(exception).isExactlyInstanceOf(IllegalArgumentException::class.java)
    }
}
