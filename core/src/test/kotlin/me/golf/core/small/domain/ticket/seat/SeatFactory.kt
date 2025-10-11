package me.golf.core.small.domain.ticket.seat

import me.golf.core.model.domain.ticket.seat.Seat
import me.golf.core.model.domain.ticket.seat.enumerate.SeatSection

object SeatFactory {

    fun create(
        id: Long? = null,
        row: Int? = null,
        floor: Int? = null,
        number: Int? = null,
        section: SeatSection? = null,
        isAvailable: Boolean? = null,
    ): Seat {
        return Seat.create(
            id = id?: 1L,
            row = row ?: 1,
            floor = floor ?: 1,
            number = number ?: 1,
            section = section ?: SeatSection.entries.toTypedArray().random(),
            isAvailable = isAvailable ?: true,
        )
    }
}
