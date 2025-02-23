package me.golf.app.service.domain.payment

import me.golf.app.common.TransactionHelper
import me.golf.app.service.domain.order.OrderRollbackHelper
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.usecase.domain.payment.PaymentUseCase
import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage
import org.springframework.stereotype.Service

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val ticketRepository: TicketRepository,
): PaymentUseCase {

    override fun payment(message: PaymentRequestMessage): PaymentResponseMessage {
        return OrderRollbackHelper.rollbackOrder(message.orderId, message.userId) {
            val order = orderRepository.findWithPaymentById(message.orderId)
            val payment = order!!.payment ?: throw IllegalArgumentException("payment must not be null")
            val completePayment = paymentRepository.confirm(payment.addIdempotentKey(message.paymentKey), order.orderId)

            TransactionHelper.execute {
                paymentRepository.update(completePayment, order.orderId)

                val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })
                val purchasedTicket = tickets.map { it.purchase() }

                ticketRepository.saveAll(purchasedTicket)
            }

            // wallet event 발생

            // ledger event 발생

            return@rollbackOrder PaymentResponseMessage(
                paymentId = completePayment.id!!,
                paymentDate = completePayment.paymentDate,
            )
        }
    }
}
