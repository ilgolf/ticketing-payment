package me.golf.infra.medium.domain.order

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.orderitem.OrderItem
import me.golf.core.model.domain.payment.Payment
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.infra.dao.domain.order.OrderItemJpaDao
import me.golf.infra.dao.domain.order.OrderJpaDao
import me.golf.infra.dao.domain.payment.PaymentJpaDao
import me.golf.infra.medium.DataJpaTestModule
import me.golf.infra.medium.util.TicketFactory
import org.junit.jupiter.api.BeforeEach
import org.springframework.beans.factory.annotation.Autowired

class OrderRepositoryTest
@Autowired
constructor(
    private val orderRepository: OrderRepository,
    private val orderJpaDao: OrderJpaDao,
    private val orderItemJpaDao: OrderItemJpaDao,
    private val paymentJpaDao: PaymentJpaDao
): DataJpaTestModule() {

    private val userId = 123L
    private lateinit var testOrder: Order
    private lateinit var testOrderItems: List<OrderItem>
    private lateinit var testPayment: Payment

    @BeforeEach
    fun setup() {
        val ticket = TicketFactory.createTicket()
    }
}