package me.golf.core.model.domain.order

enum class OrderState(
    val serializedValue: String,
    val description: String,
) {

    TRY_ORDER("tryOrder", "주문 시도"),
    SUCCESS("success", "주문 성공"),
    FAILURE("failure", "주문 실패"),
    REFUND("refund", "주문 환불")
}