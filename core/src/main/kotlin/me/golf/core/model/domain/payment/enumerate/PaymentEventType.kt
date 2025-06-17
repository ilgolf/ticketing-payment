package me.golf.core.model.domain.payment.enumerate

enum class PaymentEventType {
    PAYMENT,      // 결제 완료
    SETTLEMENT,  // 정산 시작
    LEDGER,         // 원장 처리 시작
}
