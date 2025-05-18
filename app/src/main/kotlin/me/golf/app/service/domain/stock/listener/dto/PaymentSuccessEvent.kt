package me.golf.app.service.domain.stock.listener.dto

data class PaymentSuccessEvent(
    val paymentId: Long,
    val orderId: String,
) {

}
