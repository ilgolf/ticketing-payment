package me.golf.infra.entity.domain.ticket

import jakarta.persistence.*
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.infra.entity.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "ticket")
class TicketEntity(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long,

    @Column(nullable = false)
    val seatId: Long,

    @Column(nullable = false)
    val status: String,

    @Column(nullable = false)
    val price: BigDecimal,

    @Column(nullable = false)
    val openDateTime: LocalDateTime
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as TicketEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}