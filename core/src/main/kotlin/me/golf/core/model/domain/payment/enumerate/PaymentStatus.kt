package me.golf.core.model.domain.payment.enumerate

enum class PaymentStatus(
    val serializable: String,
    val description: String,
) {
    PENDING("pending", "대기중"),
    COMPLETE("complete", "결제 완료"),
    FAIL("fail", "결제 실패"),
    REFUND("refund", "환불")
}