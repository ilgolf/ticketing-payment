package me.golf.app.service.domain.payment

import me.golf.app.common.TransactionHelper
import me.golf.app.service.domain.payment.listener.dto.LedgerEventMessage
import me.golf.app.service.domain.payment.listener.dto.WalletEventMessage
import me.golf.app.service.domain.stock.listener.dto.OrderFailEvent
import me.golf.app.service.domain.stock.listener.dto.PaymentSuccessEvent
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderState
import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.usecase.domain.payment.PaymentUseCase
import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import me.golf.core.usecase.domain.payment.request.UpdatePaymentEventStatusRequestMessage
import me.golf.core.usecase.domain.payment.response.PaymentEventStatusResponseMessage
import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage
import me.golf.infra.dao.domain.payment.PaymentEventJpaDao
import org.springframework.context.ApplicationEventPublisher
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val ticketRepository: TicketRepository,
    private val eventPublisher: ApplicationEventPublisher,
): PaymentUseCase {

    override fun payment(message: PaymentRequestMessage): PaymentResponseMessage {
        publishPaymentFailEvent(message)

        val order = orderRepository.findWithPaymentById(message.orderId)
        val payment = order.payment ?: throw IllegalArgumentException("payment must not be null")
        val completePayment = paymentRepository.confirm(payment.addIdempotentKey(message.paymentKey), order.orderId)

        TransactionHelper.execute {
            paymentPostProcess(completePayment, order)
            publishPaymentSuccessEvent(completePayment, order)
        }

        return PaymentResponseMessage(paymentId = completePayment.id!!, paymentDate = completePayment.paymentDate)
    }

    private fun publishPaymentFailEvent(message: PaymentRequestMessage) {
        eventPublisher.publishEvent(OrderFailEvent(message.orderId))
    }

    private fun paymentPostProcess(completePayment: Payment, order: Order) {
        paymentRepository.update(completePayment, order.orderId)

        val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })

        orderRepository.save(order.changeState(OrderState.SUCCESS))
        ticketRepository.saveAll(tickets.map { it.purchase() })
    }

    private fun publishPaymentSuccessEvent(completePayment: Payment, order: Order) {
        // wallet event 발생
        eventPublisher.publishEvent(WalletEventMessage(order.orderId, completePayment.id!!, order.userId))

        // ledger event 발생
        eventPublisher.publishEvent(LedgerEventMessage(order.orderId, completePayment.id!!, order.userId))

        // 선점 해제 이벤트 발생
        eventPublisher.publishEvent(PaymentSuccessEvent(completePayment.id!!, order.orderId))
    }

    @Transactional
    override fun changeEventStatus(message: UpdatePaymentEventStatusRequestMessage): PaymentEventStatusResponseMessage {
        val paymentEvent: PaymentEvent = paymentRepository.findEventByOrderId(message.orderId, message.userId)
        val updatedPaymentEvent = paymentEvent.changeEventStatus(message.eventStatus)
        paymentRepository.updateEvent(updatedPaymentEvent)

        return PaymentEventStatusResponseMessage(paymentId = updatedPaymentEvent.paymentId, paymentEventId = updatedPaymentEvent.eventId!!)
    }
}
