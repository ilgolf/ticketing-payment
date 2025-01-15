package me.golf.infra.dao.domain.stock

import org.springframework.data.redis.core.RedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

interface StockDao {
    fun saveOrderReserveInfo(orderId: Long, itemIds: Collection<Long>): Boolean
    fun existsReserveInfoByOrderId(orderId: Long): Boolean
}

@Repository
internal class StockDaoByRedis(
    private val stockRedisTemplate: RedisTemplate<String, List<Long>> // key: reserve::1 value: 1, 3, 5
) : StockDao {
    override fun saveOrderReserveInfo(orderId: Long, itemIds: Collection<Long>): Boolean {
        val isLocked = stockRedisTemplate.opsForHash<String, List<Long>>().putIfAbsent(orderId.toString(), RESERVATION_KEY, itemIds.toList())

        if (isLocked) {
            stockRedisTemplate.expire(orderId.toString(), Duration.ofMinutes(10))
        }

        return isLocked
    }

    override fun existsReserveInfoByOrderId(orderId: Long): Boolean {
        val stockInfos = stockRedisTemplate.opsForHash<String, List<Long>>().get(orderId.toString(), RESERVATION_KEY)
            ?: emptyList()

        return stockInfos.isNotEmpty()
    }

    companion object {
        const val RESERVATION_KEY = "reservation"
    }
}
