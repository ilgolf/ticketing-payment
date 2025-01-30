package me.golf.infra.client

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import me.golf.core.model.domain.payment.Payment
import me.golf.infra.client.request.TossPaymentRequestBody
import me.golf.infra.client.response.PaymentResponse
import me.golf.infra.client.response.TossPaymentErrorResponse
import me.golf.infra.client.response.TossPaymentResponse
import org.slf4j.LoggerFactory
import org.springframework.http.HttpStatusCode
import org.springframework.stereotype.Component
import org.springframework.web.client.RestClient
import org.springframework.web.client.body

interface PaymentClient {

    fun payment(payment: Payment, orderId: String): PaymentResponse
}

@Component
internal class TossPaymentClient(
    private val tossPaymentRestClient: RestClient,
    private val objectMapper: ObjectMapper,
) : PaymentClient {

    private val log = LoggerFactory.getLogger(TossPaymentClient::class.java)

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
            .body<String>() ?: throw IllegalArgumentException("fail payment")

        log.info("response: $response")

        return objectMapper.readValue<TossPaymentResponse>(response).toResponse()
    }

    companion object {
        const val CONFIRM_END_POINT = "/v1/payments/confirm"
    }
}
