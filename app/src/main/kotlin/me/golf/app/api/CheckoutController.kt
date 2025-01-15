package me.golf.app.api

import me.golf.app.api.request.CheckoutRequest
import me.golf.app.api.response.CheckoutResponse
import me.golf.core.usecase.domain.order.usecase.CheckoutUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface CheckoutController {

    fun confirm(request: CheckoutRequest): ResponseEntity<CheckoutResponse>
}

@RestController
internal class CheckoutControllerImpl(
    private val checkoutUseCase: CheckoutUseCase
): CheckoutController {

    @PostMapping("/checkout")
    override fun confirm(@RequestBody request: CheckoutRequest): ResponseEntity<CheckoutResponse> {
        val result = checkoutUseCase.checkout(request.toMessage())
        return ResponseEntity.ok(CheckoutResponse(orderId = result.orderId, orderStatus = result.orderStatus.serializedValue))
    }
}
