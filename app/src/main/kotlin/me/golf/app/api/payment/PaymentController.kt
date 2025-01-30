package me.golf.app.api.payment

import me.golf.app.api.payment.request.PaymentRequest
import me.golf.app.api.payment.response.PaymentResponse
import me.golf.core.usecase.domain.payment.PaymentUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface PaymentController {

    fun pay(request: PaymentRequest): ResponseEntity<PaymentResponse>
}

@RestController
internal class PaymentControllerImpl(
    private val useCase: PaymentUseCase
): PaymentController {

    @PostMapping("/payment")
    override fun pay(@RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val result = useCase.payment(request.toMessage())
        return ResponseEntity.ok(PaymentResponse.of(result))
    }
}
