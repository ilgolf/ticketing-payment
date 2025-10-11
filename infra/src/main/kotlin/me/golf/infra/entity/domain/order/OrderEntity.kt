package me.golf.infra.entity.domain.order

import jakarta.persistence.*
import me.golf.infra.entity.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    var id: String,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "orderDate", nullable = false)
    val orderDate: LocalDateTime,

    @Column(name = "state", nullable = false)
    val orderState: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}
