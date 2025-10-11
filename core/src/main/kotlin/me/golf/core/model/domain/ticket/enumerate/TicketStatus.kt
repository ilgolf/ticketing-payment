package me.golf.core.model.domain.ticket.enumerate

enum class TicketStatus(
    private val serializableValue: String,
    private val description: String,
) {
    DISABLED("disabled", "구매 불가능한 상태"),
    AVAILABLE("available", "현재 구매 가능한 상태"),
    SOLD("sold", "현재 구매 되어 다른 사람이 살 수 없는 상태")
}