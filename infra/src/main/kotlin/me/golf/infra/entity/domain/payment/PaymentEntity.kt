package me.golf.infra.entity.domain.payment

import jakarta.persistence.*
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.infra.entity.BaseEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

@Entity
@Table(name = "payment")
class PaymentEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id",nullable = false)
    var id: Long? = null,

    @Column(name = "amount",nullable = false)
    val amount: BigDecimal,

    @Column(name = "payment_method",nullable = false)
    val paymentMethod: String,

    @Column(name = "order_id",nullable = false)
    val orderId: String,

    @Column(name = "payment_status",nullable = false)
    val paymentStatus: String,

    @Column(name = "payment_date",nullable = false)
    val paymentDate: LocalDateTime,

    @Column(name = "idempotent_key",nullable = false)
    val idempotentKey: String,

    @Column(name = "user_id",nullable = false)
    val userId: Long,
): BaseEntity() {
}