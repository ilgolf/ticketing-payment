package me.golf.scheduler.api

import me.golf.scheduler.application.PaymentEventRetryService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class PaymentEventScheduler(
    private val paymentEventRetryService: PaymentEventRetryService
) {

    /**
     * payment event 처리 시 outbox에서 미처리된 데이터를 재처리한다. (wallet 전용)
     */
    @Scheduled(cron = "0 * * * * *")
    fun retryFailWalletEvent() {
        paymentEventRetryService.sendFailWalletEvent()
    }

    /**
     * payment event 처리 시 outbox에서 미처리된 데이터를 재처리한다. (ledger 전용)
     */
    @Scheduled(cron = "0 * * * * *")
    fun retryFailLedgerEvent() {
        paymentEventRetryService.sendFailLedgerEvent()
    }
}