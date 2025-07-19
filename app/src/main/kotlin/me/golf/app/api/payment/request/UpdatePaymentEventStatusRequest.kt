package me.golf.app.api.payment.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import me.golf.core.usecase.domain.payment.request.UpdatePaymentEventStatusRequestMessage

data class UpdatePaymentEventStatusRequest(
    @field:Positive(message = "사용자 ID는 양수여야 합니다")
    val userId: Long,

    @field:NotBlank(message = "주문 ID는 필수 값입니다.")
    val orderId: String,

    @field:NotNull(message = "수정 변경 타입은 필수 값입니다. SUCCESS: 성공, FAILURE: 실패")
    val changeRequestType: ChangeRequestType,
) {

    fun toCommand(): UpdatePaymentEventStatusRequestMessage {
        return UpdatePaymentEventStatusRequestMessage(
            userId = userId,
            orderId = orderId,
            eventStatus = changeRequestType.toEventStatus()
        )
    }
}
