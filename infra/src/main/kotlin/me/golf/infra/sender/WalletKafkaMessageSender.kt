package me.golf.infra.sender

import me.golf.core.sender.domain.wallet.WalletMessageSender
import org.springframework.context.annotation.Profile
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.stereotype.Component

@Component
@Profile("!test")
class WalletKafkaMessageSender(
    private val kafkaTemplate: KafkaTemplate<String, String>
) : WalletMessageSender {

    override fun send(paymentId: Long, userId: Long) {
        val payload =
            """
            {
                "paymentId": $paymentId,
                "userId": $userId
            }
            """.trimIndent()

        kafkaTemplate.send(WALLET_TOPIC, payload)
    }

    companion object {
        private const val WALLET_TOPIC = "wallet"
    }
}

@Component
@Profile("test")
internal class WalletDefaultMessageSender() : WalletMessageSender {

    override fun send(paymentId: Long, userId: Long) {}
}