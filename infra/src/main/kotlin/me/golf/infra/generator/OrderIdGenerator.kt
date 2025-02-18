package me.golf.infra.generator

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Component
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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

        val suffix = String.format("%04d", sequence).takeLast(4)

        val timestamp = DateTimeFormatter.ofPattern("yyyyMMddhhmmss").format(LocalDateTime.now())

        return "$timestamp-$suffix"
    }

    companion object {
        private const val GLOBAL_SEQUENCE_KEY = "global_sequence"
    }
}