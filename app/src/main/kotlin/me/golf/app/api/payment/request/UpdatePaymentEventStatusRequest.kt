package me.golf.app.api.payment.request

import me.golf.core.usecase.domain.payment.request.UpdatePaymentEventStatusRequestMessage

data class UpdatePaymentEventStatusRequest(
    val userId: Long,
    val orderId: String,
    val changeType: ChangeType,
) {

    fun toCommand(): UpdatePaymentEventStatusRequestMessage {
        return UpdatePaymentEventStatusRequestMessage(
            userId = userId,
            orderId = orderId,
            eventStatus = changeType.toEventStatus()
        )
    }
}
