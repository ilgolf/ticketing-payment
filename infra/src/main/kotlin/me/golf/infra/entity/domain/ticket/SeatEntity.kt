package me.golf.infra.entity.domain.ticket

import jakarta.persistence.*
import me.golf.infra.entity.BaseEntity

@Entity
@Table(name = "seat")
class SeatEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "seat_id", insertable = false, nullable = false, updatable = false)
    var id: Long? = null,

    @Column(nullable = false)
    val rowIndex: Int,

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