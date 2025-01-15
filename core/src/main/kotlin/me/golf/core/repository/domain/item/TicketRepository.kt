package me.golf.core.repository.domain.item

import me.golf.core.model.domain.ticket.Ticket

interface TicketRepository {

    fun findAllByOrderId(ticketIds: List<Long>): List<Ticket>
}