package me.golf.app.service.domain.payment

import me.golf.core.repository.domain.item.TicketRepository
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentRepository
import me.golf.core.usecase.domain.payment.PaymentUseCase
import me.golf.core.usecase.domain.payment.request.PaymentRequestMessage
import me.golf.core.usecase.domain.payment.response.PaymentResponseMessage
import org.springframework.stereotype.Service
import org.springframework.transaction.support.TransactionTemplate

@Service
class PaymentService(
    private val orderRepository: OrderRepository,
    private val paymentRepository: PaymentRepository,
    private val transactionTemplate: TransactionTemplate,
    private val ticketRepository: TicketRepository,
): PaymentUseCase {

    override fun payment(message: PaymentRequestMessage): PaymentResponseMessage {
        // 주문 조회
        val order = orderRepository.findWithPaymentById(message.orderId)

        // 결제 정보 조회
        val payment = order!!.payment ?: throw IllegalArgumentException("payment must not be null")

        // 결제 시도
        val completePayment = paymentRepository.confirm(payment.addIdempotentKey(message.paymentKey), order.orderId)

        // 결제 완료 후 전산 처리
        transactionTemplate.execute { status ->
            val runCatching = kotlin.runCatching {
                // 결제 정보 저장
                paymentRepository.update(completePayment, order.orderId)

                // 티켓 판매 처리
                val tickets = ticketRepository.findAllByOrderId(order.orderItem.map { it.itemId })
                val purchasedTicket = tickets.map { it.purchase() }

                ticketRepository.saveAll(purchasedTicket)
            }

            if (runCatching.isFailure) {
                status.setRollbackOnly()
            }
        }

        // wallet event 발생

        // ledger event 발생

        return PaymentResponseMessage(
            paymentId = completePayment.id!!,
            paymentDate = completePayment.paymentDate,
        )
    }
}