package me.golf.infra.config

import me.golf.infra.config.KafkaConstantPool.ACKS_VALUE
import me.golf.infra.config.KafkaConstantPool.DELIVERY_TIMEOUT_MS
import me.golf.infra.config.KafkaConstantPool.RETRIES_BACK_OFF_MS
import me.golf.infra.config.KafkaConstantPool.RETRIES_COUNT
import org.apache.kafka.clients.producer.ProducerConfig
import org.apache.kafka.common.serialization.StringSerializer
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.kafka.core.DefaultKafkaProducerFactory
import org.springframework.kafka.core.KafkaTemplate
import org.springframework.kafka.core.ProducerFactory

@Configuration
class KafkaConfig(
    @Value("\${spring.kafka.producer.bootstrap-servers}")
    private val bootstrapServers: String,
) {

    @Bean
    fun factory(): ProducerFactory<String, String> {
        val properties = HashMap<String, Any>()
        properties[ProducerConfig.BOOTSTRAP_SERVERS_CONFIG] = bootstrapServers
        properties[ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        properties[ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG] = StringSerializer::class.java
        properties[ProducerConfig.ENABLE_IDEMPOTENCE_CONFIG] = true // 명시적으로 선언
        properties[ProducerConfig.RETRIES_CONFIG] = RETRIES_COUNT
        properties[ProducerConfig.RETRY_BACKOFF_MS_CONFIG] = RETRIES_BACK_OFF_MS
        properties[ProducerConfig.DELIVERY_TIMEOUT_MS_CONFIG] = DELIVERY_TIMEOUT_MS
        properties[ProducerConfig.ACKS_CONFIG] = ACKS_VALUE

        return DefaultKafkaProducerFactory(properties)
    }

    @Bean
    fun kafkaTemplate(): KafkaTemplate<String, String> {
        return KafkaTemplate(factory())
    }
}
