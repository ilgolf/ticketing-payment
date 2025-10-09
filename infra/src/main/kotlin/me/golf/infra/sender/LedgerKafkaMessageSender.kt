package me.golf.infra.sender

import com.fasterxml.jackson.databind.ObjectMapper
import me.golf.core.sender.domain.ledger.LedgerMessageSender
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.support.SendResult
import org.springframework.stereotype.Component

@Component
class LedgerKafkaMessageSender(
    private val kafkaTemplate: KafkaTemplate<String, String>,
    private val emailSender: EmailSender,
    private val objectMapper: ObjectMapper,
): LedgerMessageSender {

    override fun send(orderId: String, userId: Long, traceId: String) {
        val payload = LedgerPayload(orderId, userId)

        kafkaTemplate.send(LEDGER_TOPIC, payload.toJson())
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

    private fun handleKafkaResult(
        result: SendResult<String, String>?,
        ex: Throwable?,
        orderId: String,
        userId: Long,
        traceId: String
    ) {
        if (ex != null) {
            log.error("❌ledger 이벤트 처리 실패 orderId: {}, traceId: {}", orderId, traceId)
            emailSender.send(orderId, userId, traceId)
            return
        }

        log.info("✅ Kafka 성공: ${result?.recordMetadata?.offset()}")
    }

    private fun LedgerPayload.toJson() = objectMapper.writeValueAsString(this)

    companion object {
        private val log: Logger = LoggerFactory.getLogger(LedgerKafkaMessageSender::class.java)
        private const val LEDGER_TOPIC = "ledger"
    }
}

@Component
@Profile("test")
class LedgerDefaultMessageSender : LedgerMessageSender {

    override fun send(orderId: String, userId: Long, traceId: String) {}
}