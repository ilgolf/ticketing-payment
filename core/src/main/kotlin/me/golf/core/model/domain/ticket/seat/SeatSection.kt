package me.golf.core.model.domain.ticket.seat

enum class SeatSection(
    val serializableValue: String,
    val description: String
) {
    P("p", "지정석 VVIP석"),
    STANDING("standing", "스탠딩 구역"),
    R("r", "지정석 Royal 석"),
    S("s", "지정석 VIP석"),
    A("a", "지정석 A"),
    B("b", "지정석 B")
}