package me.golf.app.service.domain.payment.listener

import me.golf.app.service.domain.payment.listener.dto.WalletEventMessage
import me.golf.core.sender.domain.wallet.WalletMessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class WalletListener(
    private val walletMessageSender: WalletMessageSender
) {

    private val log : Logger = LoggerFactory.getLogger(WalletListener::class.java)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun onPayment(event: WalletEventMessage) {
        log.info("결제 완료 -> 정산 정보 입력 이벤트 전송 paymentId : {}", event.paymentId)
        walletMessageSender.send(event.paymentId, event.userId)
    }
}