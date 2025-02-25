package me.golf.app.service.domain.payment

import me.golf.app.common.TransactionHelper
import me.golf.app.service.domain.stock.listener.dto.OrderFailEvent
import me.golf.core.model.domain.order.OrderState
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.usecase.domain.payment.PaymentUseCase
import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val ticketRepository: TicketRepository,
    private val eventPublisher: ApplicationEventPublisher
): PaymentUseCase {

    override fun payment(message: PaymentRequestMessage): PaymentResponseMessage {
        eventPublisher.publishEvent(OrderFailEvent(message.orderId))

        val order = orderRepository.findWithPaymentById(message.orderId)
            ?: throw IllegalArgumentException("Order with id ${message.orderId} not found")

        val payment = order.payment ?: throw IllegalArgumentException("payment must not be null")
        val completePayment = paymentRepository.confirm(payment.addIdempotentKey(message.paymentKey), order.orderId)

        TransactionHelper.execute {
            paymentRepository.update(completePayment, order.orderId)

            val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })

            orderRepository.save(order.changeState(OrderState.SUCCESS))
            ticketRepository.saveAll(tickets.map { it.purchase() })
        }

        // wallet event 발생

        // ledger event 발생

        return PaymentResponseMessage(paymentId = completePayment.id!!, paymentDate = completePayment.paymentDate)
    }
}
