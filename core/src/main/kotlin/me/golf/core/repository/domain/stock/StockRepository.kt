package me.golf.core.repository.domain.stock

interface StockRepository {

    fun reserveStock(orderId: Long, itemIds: Collection<Long>): Boolean
    fun alreadyReserve(orderId: Long): Boolean
}