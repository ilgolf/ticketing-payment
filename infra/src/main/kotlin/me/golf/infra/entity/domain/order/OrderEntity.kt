package me.golf.infra.entity.domain.order

import jakarta.persistence.*
import me.golf.core.model.domain.order.OrderState
import me.golf.infra.entity.BaseEntity
import java.math.BigDecimal
import java.time.Instant

@Entity
@Table(name = "orders")
class OrderEntity(
    @Id
    @GeneratedValue
    @Column(name = "order_id", nullable = false, insertable = false, updatable = false)
    var id: Long? = null,

    @Column(name = "amount", nullable = false)
    val amount: BigDecimal,

    @Column(name = "orderDate", nullable = false)
    val orderDate: Instant,

    @Column(name = "state", nullable = false)
    val orderState: String,

    @Column(name = "user_id", nullable = false)
    val userId: Long,

    @Column(name = "payment_id")
    val paymentId: Long? = null,
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as OrderEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}
