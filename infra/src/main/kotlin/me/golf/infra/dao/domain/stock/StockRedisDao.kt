package me.golf.infra.dao.domain.stock

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

interface StockDao {
    fun saveOrderReserveInfo(orderId: String, itemIds: Collection<Long>, ttl: Long): Boolean
    fun existsReserveInfoByOrderId(orderId: String, ticketIds: Collection<Long>): Boolean
    fun existsReserveByOrderId(orderId: String): Boolean
    fun updateTtl(orderId: String, ttl: Long)
}

@Repository
internal class StockDaoByRedis(
    private val stockRedisTemplate: RedisTemplate<String, String> // key: reserve::1 value: 1, 3, 5
) : StockDao {
    override fun saveOrderReserveInfo(orderId: String, itemIds: Collection<Long>, ttl: Long): Boolean {
        val isLocked = stockRedisTemplate.opsForHash<String, String>().putIfAbsent(RESERVATION_KEY, orderId, itemIds.joinToString(","))

        if (isLocked) {
            stockRedisTemplate.expire(orderId, Duration.ofMinutes(ttl))
        }

        return isLocked
    }

    override fun existsReserveInfoByOrderId(orderId: String, ticketIds: Collection<Long>): Boolean {
        val stockInfos = stockRedisTemplate.opsForHash<String, String>().entries(RESERVATION_KEY).asSequence()
            .filter { (key, _) -> orderId != key }
            .map { (_, value) -> value.split(",") }
            .flatten()
            .map { it.toLong() }
            .toSet()

        if (stockInfos.isEmpty()) {
            return false
        }

        val result = ticketIds.filter { stockInfos.contains(it) }.toList()

        return result.isNotEmpty()
    }

    override fun existsReserveByOrderId(orderId: String): Boolean {
        return stockRedisTemplate.opsForHash<String, String>().get(RESERVATION_KEY, orderId) != null
    }

    override fun updateTtl(orderId: String, ttl: Long) {
        stockRedisTemplate.expire(orderId, Duration.ofMinutes(ttl))
    }

    companion object {
        private const val RESERVATION_KEY = "reservation"
    }
}
