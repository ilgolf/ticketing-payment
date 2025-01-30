package me.golf.core.model.domain.payment

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

interface Payment {

    /**
     * 결제 식별자
     */
    val id: Long?

    /**
     * 총 결제 금액
     */
    val amount: BigDecimal

    /**
     * 결제 방법
     */
    val paymentMethod: PaymentMethod

    /**
     * 결제 상태
     */
    val paymentStatus: PaymentStatus

    /**
     * 결제일
     */
    val paymentDate: LocalDateTime

    /**
     * 멱등키 - 결제 시 중복 결제를 막기 위한 멱등키입니다.
     */
    val idempotentKey: String

    /**
     * 결제인 ID
     */
    val userId: Long

    fun addIdempotentKey(idempotentKey: String): Payment

    companion object {

        fun create(
            id: Long? = null,
            amount: BigDecimal,
            paymentMethod: PaymentMethod,
            paymentStatus: PaymentStatus,
            paymentDate: LocalDateTime,
            idempotentKey: String,
            userId: Long
        ) =
            PaymentMutator(
                id = id,
                amount = amount,
                paymentMethod = paymentMethod,
                paymentStatus = paymentStatus,
                paymentDate = paymentDate,
                idempotentKey = idempotentKey,
                userId = userId
            )
    }
}

class PaymentMutator(
    override val id: Long?,
    override val amount: BigDecimal,
    override val paymentMethod: PaymentMethod,
    override val paymentStatus: PaymentStatus,
    override val paymentDate: LocalDateTime,
    override val idempotentKey: String,
    override val userId: Long,
): Payment {

    override fun addIdempotentKey(idempotentKey: String): Payment {
        return Payment.create(
            this.id,
            this.amount,
            this.paymentMethod,
            this.paymentStatus,
            this.paymentDate,
            idempotentKey,
            this.userId,
        )
    }
}
