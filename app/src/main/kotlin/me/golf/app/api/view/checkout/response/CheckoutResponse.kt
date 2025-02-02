package me.golf.app.api.checkout.response

data class CheckoutResponse(
    val orderId: String,
    val orderStatus: String,
)