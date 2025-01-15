package me.golf.infra.dao.domain.item

import me.golf.infra.entity.domain.ticket.TicketEntity
import org.springframework.data.jpa.repository.JpaRepository

interface TicketJpaDao: JpaRepository<TicketEntity, Long> {
    fun findByIdIn(ids: MutableCollection<Long>): MutableList<TicketEntity>
}