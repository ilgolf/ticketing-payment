package me.golf.app.api.order

import jakarta.validation.Valid
import me.golf.app.api.order.request.CreateOrderRequest
import me.golf.app.api.order.response.CreateOrderResponse
import me.golf.core.usecase.domain.order.usecase.OrderUseCase
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RestController

interface OrderController {

    fun order(request: CreateOrderRequest): ResponseEntity<CreateOrderResponse>
}

@RestController
internal class OrderControllerImpl(
    private val orderUseCase: OrderUseCase
): OrderController {

    @PostMapping("/orders")
    override fun order(@Valid @RequestBody request: CreateOrderRequest): ResponseEntity<CreateOrderResponse> {
        val result = orderUseCase.order(request.ticketIds, request.userId)

        return ResponseEntity.ok(CreateOrderResponse.from(result))
    }
}