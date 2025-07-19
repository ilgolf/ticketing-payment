package me.golf.app.api.payment.request

import me.golf.core.model.domain.payment.enumerate.EventStatus

enum class ChangeRequestType(
    val description: String,
) {
    SUCCESS("성공 요청"),
    FAILURE("실패 요청"),
    ;

    fun toEventStatus(): EventStatus {
        return when (this) {
            SUCCESS -> EventStatus.COMPLETED
            FAILURE -> EventStatus.FAILED
        }
    }
}