package me.golf.infra.client.response

import me.golf.core.model.domain.payment.Payment
import me.golf.core.model.domain.payment.PaymentMethod
import me.golf.core.model.domain.payment.PaymentStatus
import me.golf.infra.entity.domain.payment.PaymentEntity
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.UUID

data class PaymentResponse(
    val merchantId: String,
    val lastTransactionKey: String,
    val idempotentKey: String,
    val status: String,
    val requestedAt: LocalDateTime,
    val approvedAt: LocalDateTime,
    val cardInfo: Card?,
    val virtualAccountInfo: VirtualAccount?,
    val secret: String?,
    val type: String,
    val amount: BigDecimal
) {
    fun toEntity(payment: Payment, orderId: String): PaymentEntity {
        if (this.status !== "") {
            throw IllegalArgumentException("")
        }

        return PaymentEntity(
            id = payment.id,
            amount = this.amount,
            paymentMethod = getPaymentMethod().name,
            paymentStatus = PaymentStatus.COMPLETE.name,
            paymentDate = this.approvedAt,
            idempotentKey = this.idempotentKey,
            userId = payment.userId,
            orderId = orderId,
        )
    }

    private fun getPaymentMethod(): PaymentMethod {
        return when {
            this.cardInfo != null -> PaymentMethod.CARD
            this.virtualAccountInfo != null -> PaymentMethod.CASH
            else -> PaymentMethod.POINT
        }
    }
}
