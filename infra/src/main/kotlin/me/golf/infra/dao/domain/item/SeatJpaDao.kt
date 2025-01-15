package me.golf.infra.dao.domain.item

import me.golf.infra.entity.domain.ticket.SeatEntity
import org.springframework.data.jpa.repository.JpaRepository

interface SeatJpaDao: JpaRepository<SeatEntity, Long> {
    fun findByIdIn(ids: MutableCollection<Long>): MutableList<SeatEntity>

}
