package me.golf.app.service.domain.payment.listener

import me.golf.app.service.domain.payment.listener.dto.LedgerEventMessage
import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import me.golf.core.repository.domain.payment.PaymentEventRepository
import me.golf.core.sender.domain.ledger.LedgerMessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.*

@Component
class LedgerSubscriber(
    private val paymentEventRepository: PaymentEventRepository,
    private val ledgerMessageSender: LedgerMessageSender
) {
    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleSuccessPaymentEvent(event: LedgerEventMessage) {
        log.info("결제 완료 원장 정보 저장 Event: {}", event.paymentId)

        val paymentEvent = PaymentEvent.create(
            paymentId = event.paymentId,
            eventType = PaymentEventType.LEDGER
        )

        paymentEventRepository.save(paymentEvent)

        sendLedgerEvent(event)
    }

    private fun sendLedgerEvent(event: LedgerEventMessage) {
        val traceId: UUID = UUID.randomUUID()
        log.info("원장 정보 입력 메시지 전송 시작 orderId: {} traceId: {}", event.orderId, traceId)

        val result = kotlin.runCatching { ledgerMessageSender.send(event.orderId, event.userId, traceId.toString()) }

        result.onFailure { log.error("원장 정보 입력 메시지 전송 실패 orderId: {}, traceId: {}", event.paymentId, traceId, it) }
    }
}