package me.golf.infra.sender

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.core.sender.domain.wallet.WalletMessageSender
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class WalletKafkaMessageSender(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val objectMapper: ObjectMapper,
    private val emailSender: EmailSender
) : WalletMessageSender {

    override fun send(orderId: String, userId: Long, traceId: String) {
        val payload = WalletPayload(orderId, userId)

        kafkaTemplate.send(WALLET_TOPIC, payload.toJson())
            .whenComplete { result, ex ->
                handleKafkaResult(
                    result = result,
                    ex = ex,
                    orderId = orderId,
                    userId = userId,
                    traceId = traceId
                )
            }
    }

    private fun WalletPayload.toJson() = objectMapper.writeValueAsString(this)

    private fun handleKafkaResult(
        result: SendResult<String, String>?,
        ex: Throwable?,
        orderId: String,
        userId: Long,
        traceId: String
    ) {
        if (ex != null) {
            log.error("❌wallet 이벤트 처리 실패 orderId: {}, traceId: {}", orderId, traceId)
            emailSender.send(orderId, userId, traceId)
            return
        }
        log.info("✅ Kafka 성공: ${result?.recordMetadata?.offset()}")
    }

    companion object {
        private val log = LoggerFactory.getLogger(WalletKafkaMessageSender::class.java)
        private const val WALLET_TOPIC = "wallet"
    }
}

@Component
@Profile("test")
internal class WalletDefaultMessageSender : WalletMessageSender {

    override fun send(orderId: String, userId: Long, traceId: String) {}
}