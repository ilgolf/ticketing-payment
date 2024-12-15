package me.golf.infra.entity.domain.ticket

import jakarta.persistence.*
import me.golf.core.model.domain.ticket.seat.SeatSection
import me.golf.infra.entity.BaseEntity

@Entity
@Table(name = "seat")
class SeatEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(nullable = false)
    val id: Long,

    @Column(nullable = false)
    val row: Int,

    @Column(nullable = false)
    val floor: Int,

    @Column(nullable = false)
    val number: Int,

    @Column(nullable = false)
    val section: String,

    @Column(name = "is_available",nullable = false)
    val isAvailable: Boolean,
): BaseEntity() {
}