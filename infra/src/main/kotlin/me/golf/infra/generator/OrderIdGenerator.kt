package me.golf.infra.generator

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.text.SimpleDateFormat
import java.time.LocalDateTime

interface OrderIdGenerator {

    fun generateOrderId(): String
}

@Component
internal class OrderIdGeneratorImpl(
    private val idGeneratorTemplate: RedisTemplate<String, String>,
) : OrderIdGenerator {

    override fun generateOrderId(): String {
        val sequence = idGeneratorTemplate.opsForValue().increment(GLOBAL_SEQUENCE_KEY)
            ?: throw IllegalArgumentException("fail get sequence")

        val timestamp = DATE_FORMATTER.format(LocalDateTime.now())

        return "$sequence-$timestamp"
    }

    companion object {
        private const val GLOBAL_SEQUENCE_KEY = "global_sequence"
        private val DATE_FORMATTER = SimpleDateFormat("yyyyMMdd")
    }
}