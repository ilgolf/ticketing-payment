package me.golf.core.small.domain.ticket

import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.model.domain.ticket.enumerate.TicketStatus
import me.golf.core.model.domain.ticket.seat.Seat
import java.math.BigDecimal
import java.time.LocalDateTime

object TicketFactory {

    fun create(
        id: Long? = null,
        seat: Seat,
        status: TicketStatus? = null,
        price: BigDecimal? = null,
        openDateTime: LocalDateTime? = null,
        createdAt: LocalDateTime? = null,
        lastModifiedAt: LocalDateTime? = null,
    ): Ticket {
        return Ticket.create(
            id = id?: 1L,
            seat = seat,
            status = status?: TicketStatus.AVAILABLE,
            price = price?: BigDecimal(1000.0),
            openDateTime = openDateTime?: LocalDateTime.now(),
            createdAt = createdAt?: LocalDateTime.now(),
            lastModifiedAt = lastModifiedAt?: LocalDateTime.now(),
        )
    }
}