package me.golf.core.model.domain.payment

enum class PaymentMethod(
    val serializable: String,
    val description: String,
) {
    CARD("card", "카드사 결제"),
    CASH("cash", "무통장 입금"),
    EASY_PAY("easyPay", "간편결제"),
    POINT("point", "포인트 결제")
}