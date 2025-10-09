package me.golf.infra.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.golf.core.model.domain.payment.Payment
import me.golf.infra.client.enum.EasyPayCorpCode
import me.golf.infra.client.request.TossPaymentRequestBody
import me.golf.infra.client.response.*
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body
import java.math.BigDecimal
import java.time.LocalDateTime

interface PaymentClient {

    fun payment(payment: Payment, orderId: String): PaymentResponse
}

@Component
@Profile("test")
internal class DefaultPaymentClient : PaymentClient {
    override fun payment(payment: Payment, orderId: String): PaymentResponse {
        log.info("Testing payment for order $orderId")

        return PaymentResponse(
            merchantId = "merchantId",
            lastTransactionKey = "lastTransactionKey",
            idempotentKey = "idempotentKey",
            status = TossPaymentResultStatus.DONE,
            requestedAt = LocalDateTime.now(),
            approvedAt = LocalDateTime.now(),
            easyPay = EasyPay(
                provider = EasyPayCorpCode.TOSSPAY,
                amount = BigDecimal(1_000_000),
                discountAmount = BigDecimal.ZERO,
            ),
            secret = "secret",
            type = "type",
            amount = BigDecimal(1_000_000),
        )
    }

    companion object {
        private val log = LoggerFactory.getLogger(PaymentClient::class.java)
    }
}

@Component
@Profile("!test")
internal class TossPaymentClient(
    private val tossPaymentRestClient: RestClient,
    private val objectMapper: ObjectMapper,
) : PaymentClient {

    override fun payment(payment: Payment, orderId: String): PaymentResponse {
        val requestBody = TossPaymentRequestBody(
            paymentKey = payment.idempotentKey,
            amount = payment.amount,
            orderId = orderId,
        )

        val body = objectMapper.writeValueAsString(requestBody)

        val response = tossPaymentRestClient.post()
            .uri(CONFIRM_END_POINT)
            .body(body)
            .retrieve()
            .onStatus(HttpStatusCode::is4xxClientError) { _, response ->
                val errorResponse = response.body.use { objectMapper.readValue(it, TossPaymentErrorResponse::class.java) }
                throw IllegalStateException(errorResponse.code.message)
            }
            .body<TossPaymentResponse>() ?: throw IllegalArgumentException("fail payment")

        log.info("response: $response")

        return response.toResponse()
    }

    companion object {
        private val log = LoggerFactory.getLogger(TossPaymentClient::class.java)
        const val CONFIRM_END_POINT = "/v1/payments/confirm"
    }
}
