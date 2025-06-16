package me.golf.infra.client.response

import me.golf.core.model.domain.payment.enumerate.PaymentMethod
import java.math.BigDecimal
import java.time.LocalDateTime

data class PaymentResponse(
    val merchantId: String,
    val lastTransactionKey: String,
    val idempotentKey: String,
    val status: TossPaymentResultStatus,
    val requestedAt: LocalDateTime,
    val approvedAt: LocalDateTime,
    val cardInfo: Card? = null,
    val virtualAccountInfo: VirtualAccount? = null,
    val easyPay: EasyPay? = null,
    val secret: String? = null,
    val type: String,
    val amount: BigDecimal
) {

    init {
        if (this.status == TossPaymentResultStatus.EXPIRED) {
            throw IllegalArgumentException("결제가 만료되었습니다. 다시 시도 해주세요")
        }

        if (this.status == TossPaymentResultStatus.ABORTED) {
            throw IllegalArgumentException("결제 승인에 실패하였습니다. 문의 부탁드립니다.")
        }
    }

    fun getPaymentMethod(): PaymentMethod {
        return when {
            this.cardInfo != null -> PaymentMethod.CARD
            this.virtualAccountInfo != null -> PaymentMethod.CASH
            this.easyPay != null -> PaymentMethod.EASY_PAY
            else -> PaymentMethod.POINT
        }
    }
}

enum class TossPaymentResultStatus(
    val description: String,
) {
    READY("결제 대기 상태"),
    IN_PROGRESS("결제 진행 중"),
    WAITING_FOR_DEPOSIT("가상 계좌 시에만 사용되는 입금 대기 상태"),
    DONE("결제 완료"),
    CANCELLED("승인된 결제 취소"),
    PARTIAL_CANCELLED("결제 부분 취소"),
    ABORTED("결제 승인 실패"),
    EXPIRED("결제 EXPIRED"),
}

