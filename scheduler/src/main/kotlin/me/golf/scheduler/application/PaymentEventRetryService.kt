package me.golf.scheduler.application

import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.model.domain.payment.enumerate.EventStatus
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import me.golf.core.repository.domain.order.OrderRepository
import me.golf.core.repository.domain.payment.PaymentEventRepository
import me.golf.core.sender.domain.ledger.LedgerMessageSender
import me.golf.core.sender.domain.wallet.WalletMessageSender
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.UUID

@Service
class PaymentEventRetryService(
    private val paymentEventRepository: PaymentEventRepository,
    private val orderRepository: OrderRepository,
    private val walletMessageSender: WalletMessageSender,
    private val ledgerMessageSender: LedgerMessageSender,
) {

    @Transactional(readOnly = true)
    fun sendFailWalletEvent() {
        val order: List<Order> = getOrders(PaymentEventType.SETTLEMENT)

        order.forEach {
            val traceId: String = UUID.randomUUID().toString()
            walletMessageSender.send(orderId =  it.orderId, userId = it.userId, traceId = traceId)
        }
    }

    @Transactional(readOnly = true)
    fun sendFailLedgerEvent() {
        val order: List<Order> = getOrders(PaymentEventType.LEDGER)

        order.forEach {
            val traceId: String = UUID.randomUUID().toString()
            ledgerMessageSender.send(orderId =  it.orderId, userId = it.userId, traceId = traceId)
        }
    }

    private fun getOrders(type: PaymentEventType): List<Order> {
        val paymentEvents: List<PaymentEvent> =
            paymentEventRepository.findByStatusAndType(status = EventStatus.FAILED, type = type)

        val changeEventStatus = paymentEvents.map { it.changeEventStatus(EventStatus.RETRY) }
        paymentEventRepository.saveAll(changeEventStatus)

        return orderRepository.findByPaymentId(paymentEvents.map { it.paymentId })
    }
}
