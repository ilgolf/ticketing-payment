package me.golf.core.model.domain.payment

import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

interface Payment {

    /**
     * 결제 식별자
     */
    val id: Long

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
     * 멱등키 - 토스페이먼츠에 보낼 멱등키입니다.
     */
    val idempotentKey: UUID

    /**
     * 결제인 ID
     */
    val userId: Long

    companion object {

        fun create(
            id: Long,
            amount: BigDecimal,
            paymentMethod: PaymentMethod,
            paymentStatus: PaymentStatus,
            paymentDate: LocalDateTime,
            idempotentKey: UUID,
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
    override val id: Long,
    override val amount: BigDecimal,
    override val paymentMethod: PaymentMethod,
    override val paymentStatus: PaymentStatus,
    override val paymentDate: LocalDateTime,
    override val idempotentKey: UUID,
    override val userId: Long,
): Payment
