package me.golf.infra.entity.domain.payment

import jakarta.persistence.*
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.infra.entity.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime

@Entity
@Table(name = "payment")
class PaymentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id: Long?,

    @Column(name = "amount",nullable = false)
    val amount: BigDecimal,

    @Column(name = "payment_method",nullable = false)
    @Enumerated(EnumType.STRING)
    val paymentMethod: PaymentMethod,

    @Column(name = "order_id",nullable = false)
    val orderId: String,

    @Column(name = "payment_status",nullable = false)
    @Enumerated(EnumType.STRING)
    val paymentStatus: PaymentStatus,

    @Column(name = "payment_date",nullable = false)
    val paymentDate: LocalDateTime,

    @Column(name = "idempotent_key",nullable = false)
    val idempotentKey: String,

    @Column(name = "user_id",nullable = false)
    val userId: Long,
): BaseEntity() {

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as PaymentEntity

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }
}