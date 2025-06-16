package me.golf.infra.client.response

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.core.JsonParser
import com.fasterxml.jackson.databind.DeserializationContext
import com.fasterxml.jackson.databind.JsonDeserializer
import com.fasterxml.jackson.databind.annotation.JsonDeserialize
import java.math.BigDecimal
import java.time.LocalDateTime
import java.time.OffsetDateTime

data class TossPaymentResponse(
    val mId: String,
    val lastTransactionKey: String,
    val paymentKey: String,
    val orderId: String,
    val orderName: String,
    val taxExemptionAmount: BigDecimal,
    val status: String,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @field:JsonDeserialize(using = ZonedDateTimeToLocalDateTimeDeserializer::class)
    val requestedAt: LocalDateTime,
    @field:JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm:ssXXX")
    @field:JsonDeserialize(using = ZonedDateTimeToLocalDateTimeDeserializer::class)
    val approvedAt: LocalDateTime,
    val card: Card?,
    val virtualAccount: VirtualAccount?,
    val secret: String?,
    val type: String,
    val totalAmount: BigDecimal,
) {

    fun toResponse(): PaymentResponse =
        PaymentResponse(
            merchantId = this.mId,
            lastTransactionKey = this.lastTransactionKey,
            idempotentKey = this.paymentKey,
            status = TossPaymentResultStatus.valueOf(this.status),
            requestedAt = this.requestedAt,
            approvedAt = this.approvedAt,
            cardInfo = this.card,
            virtualAccountInfo = this.virtualAccount,
            secret = this.secret,
            type = this.type,
            easyPay = null,
            amount = this.totalAmount,
        )
}

internal class ZonedDateTimeToLocalDateTimeDeserializer : JsonDeserializer<LocalDateTime>() {
    override fun deserialize(p: JsonParser, ctxt: DeserializationContext): LocalDateTime {
        val offsetDateTime = OffsetDateTime.parse(p.text)
        return offsetDateTime.toLocalDateTime()
    }
}
