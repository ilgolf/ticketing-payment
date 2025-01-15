package me.golf.infra.repository.domain.item

import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.repository.domain.item.TicketRepository
import me.golf.infra.converter.toModel
import me.golf.infra.dao.domain.item.SeatJpaDao
import me.golf.infra.dao.domain.item.TicketJpaDao
import me.golf.infra.entity.domain.ticket.TicketEntity
import org.springframework.stereotype.Repository

@Repository
class TicketRepositoryImpl(
    private val ticketJpaDao: TicketJpaDao,
    private val seatJpaDao: SeatJpaDao
) : TicketRepository {

    override fun findAllByOrderId(ticketIds: List<Long>): List<Ticket> {
        val ticketEntities: Map<Long, TicketEntity> = ticketJpaDao.findByIdIn(ticketIds.toCollection(mutableListOf())).associateBy { it.seatId }
        val seatEntities = seatJpaDao.findByIdIn(ticketEntities.map { it.key }.toCollection(mutableListOf()))

        return seatEntities.mapNotNull { seatEntity -> ticketEntities[seatEntity.id]?.toModel(seatEntity.toModel()) }
    }
}
