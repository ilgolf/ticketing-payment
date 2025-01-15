package me.golf.infra.entity.domain.order

import jakarta.persistence.*
import me.golf.infra.entity.BaseEntity

@Entity
class OrderItemEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(nullable = false)
    val orderId: Long,

    @Column(nullable = false)
    val ticketId: Long,
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderItemEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}