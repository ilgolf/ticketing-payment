package me.golf.app.service.domain.payment.listener

import me.golf.app.service.domain.payment.listener.dto.WalletEventMessage
import me.golf.core.model.domain.payment.PaymentEvent
import me.golf.core.model.domain.payment.enumerate.PaymentEventType
import me.golf.core.repository.domain.payment.PaymentEventRepository
import me.golf.core.sender.domain.wallet.WalletMessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener
import java.util.UUID

@Component
class WalletSubscriber(
    private val walletMessageSender: WalletMessageSender,
    private val paymentEventRepository: PaymentEventRepository
) {

    private val log: Logger = LoggerFactory.getLogger(this::class.java)

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onPayment(event: WalletEventMessage) {
        log.info("결제 완료 -> 정산 정보 입력 이벤트 전송 orderId : {}", event.orderId)

        val paymentEvent = PaymentEvent.create(
            paymentId = event.paymentId,
            eventType = PaymentEventType.SETTLEMENT,
        )

        paymentEventRepository.save(paymentEvent)
        sendWalletMessage(event)
    }

    private fun sendWalletMessage(event: WalletEventMessage) {
        val traceId: UUID = UUID.randomUUID()
        log.info("정산 정보 입력 메시지 전송 시작 orderId: {}, traceId: {}", event.paymentId, traceId)
        val result = kotlin.runCatching { walletMessageSender.send(event.orderId, event.userId, traceId.toString()) }

        result.onFailure { log.error("정산 정보 입력 메시지 전송 실패 orderId: {}, traceId: {}", event.orderId, traceId, it) }
    }
}