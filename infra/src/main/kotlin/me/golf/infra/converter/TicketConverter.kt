package me.golf.infra.converter

import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.core.model.domain.ticket.seat.Seat
import me.golf.core.model.domain.ticket.seat.SeatSection
import me.golf.infra.entity.domain.ticket.SeatEntity
import me.golf.infra.entity.domain.ticket.TicketEntity

fun TicketEntity.toModel(seat: Seat) = Ticket.create(
    id = this.id,
    seat = seat,
    status = TicketStatus.valueOf(this.status),
    price = this.price,
    openDateTime = this.openDateTime,
    createdAt = this.createdDate!!,
    lastModifiedAt = this.modifiedDate!!,
)

fun SeatEntity.toModel() = Seat.create(
    id = this.id,
    row = this.rowIndex,
    floor = this.floor,
    number = this.number,
    section = SeatSection.valueOf(this.section),
    isAvailable = this.isAvailable,
)