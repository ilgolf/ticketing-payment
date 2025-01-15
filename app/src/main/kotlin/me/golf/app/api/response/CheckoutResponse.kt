package me.golf.app.api.response

data class CheckoutResponse(
    val orderId: Long,
    val orderStatus: String,
)