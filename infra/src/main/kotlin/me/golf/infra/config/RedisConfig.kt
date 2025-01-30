package me.golf.infra.config

import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.RedisTemplate
import org.springframework.data.redis.serializer.RedisSerializer
import org.springframework.data.redis.serializer.SerializationException
import org.springframework.data.redis.serializer.StringRedisSerializer
import java.nio.ByteBuffer
import java.nio.charset.StandardCharsets

@Configuration
class RedisConfig(
    @Value("\${spring.data.redis.host}") private val redisHost: String,
    @Value("\${spring.data.redis.port}") private val redisPort: Int,
) {

    @Bean
    fun redisConnectionFactory() = LettuceConnectionFactory(redisHost, redisPort)

    @Bean
    fun stockRedisTemplate(): RedisTemplate<String, List<Long>> {
        val template = RedisTemplate<String, List<Long>>()
        template.connectionFactory = redisConnectionFactory()

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = LongListRedisSerializer()
        template.hashKeySerializer = StringRedisSerializer()
        template.hashValueSerializer = LongListRedisSerializer()

        template.afterPropertiesSet()

        return template
    }

    @Bean
    fun idGeneratorTemplate(): RedisTemplate<String, String> {
        val template = RedisTemplate<String, String>()
        template.connectionFactory = redisConnectionFactory()

        template.keySerializer = StringRedisSerializer()
        template.valueSerializer = StringRedisSerializer()

        return template
    }
}

internal class LongListRedisSerializer : RedisSerializer<List<Long>> {
    override fun serialize(list: List<Long>?): ByteArray? {
        return try {
            list?.joinToString(",") { it.toString() }?.toByteArray(StandardCharsets.UTF_8)
        } catch (e: Exception) {
            throw SerializationException("Could not serialize Long list: $list", e)
        }
    }

    override fun deserialize(bytes: ByteArray?): List<Long>? {
        return try {
            bytes?.toString(StandardCharsets.UTF_8)
                ?.split(",")
                ?.filter { it.isNotBlank() }
                ?.map { it.toLong() }
        } catch (e: Exception) {
            throw SerializationException("Could not deserialize Long list: ${bytes?.contentToString()}", e)
        }
    }
}
