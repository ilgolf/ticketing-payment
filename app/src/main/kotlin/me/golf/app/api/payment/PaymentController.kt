package me.golf.app.api.payment

import jakarta.validation.Valid
import me.golf.app.api.payment.request.PaymentRequest
import me.golf.app.api.payment.request.UpdatePaymentEventStatusRequest
import me.golf.app.api.payment.response.PaymentResponse
import me.golf.core.usecase.domain.payment.PaymentUseCase
import me.golf.core.usecase.domain.payment.response.PaymentEventStatusResponseMessage
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

interface PaymentController {

    fun pay(request: PaymentRequest): ResponseEntity<PaymentResponse>
    fun changeEventStatus(request: UpdatePaymentEventStatusRequest): ResponseEntity<Void>
}

@RestController
@RequestMapping("/api/v1/payments")
internal class PaymentControllerImpl(
    private val useCase: PaymentUseCase
): PaymentController {

    @PostMapping
    override fun pay(@Valid @RequestBody request: PaymentRequest): ResponseEntity<PaymentResponse> {
        val result = useCase.payment(request.toMessage())
        return ResponseEntity.ok(PaymentResponse.of(result))
    }

    @PatchMapping("/event-status")
    override fun changeEventStatus(@Valid @RequestBody request: UpdatePaymentEventStatusRequest): ResponseEntity<Void> {
        useCase.changeEventStatus(request.toCommand())
        return ResponseEntity.ok().build()
    }
}
