package me.golf.infra.entity.converter

import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.model.domain.ticket.enumerate.TicketStatus
import me.golf.core.model.domain.ticket.seat.Seat
import me.golf.core.model.domain.ticket.seat.enumerate.SeatSection
import me.golf.infra.entity.domain.ticket.SeatEntity
import me.golf.infra.entity.domain.ticket.TicketEntity

fun Ticket.toEntity() = TicketEntity(
    id = this.id,
    seatId = this.seat.id,
    status = this.status.name,
    price = this.price,
    openDateTime = this.openDateTime,
)

fun TicketEntity.toModel(seat: Seat) = Ticket.create(
    id = this.id!!,
    seat = seat,
    status = TicketStatus.valueOf(this.status),
    price = this.price,
    openDateTime = this.openDateTime,
    createdAt = this.createdDate!!,
    lastModifiedAt = this.modifiedDate!!,
)

fun SeatEntity.toModel() = Seat.create(
    id = this.id!!,
    row = this.rowIndex,
    floor = this.floor,
    number = this.number,
    section = SeatSection.valueOf(this.section),
    isAvailable = this.isAvailable,
)