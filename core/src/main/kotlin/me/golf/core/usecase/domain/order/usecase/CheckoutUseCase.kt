package me.golf.core.usecase.domain.order.usecase

import me.golf.core.usecase.domain.order.usecase.message.CheckoutCompleteResponseMessage
import me.golf.core.usecase.domain.order.usecase.message.CheckoutRequestMessage

interface CheckoutUseCase {

    fun checkout(message: CheckoutRequestMessage): CheckoutCompleteResponseMessage
}
