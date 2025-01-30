package me.golf.core.repository.domain.stock

interface StockRepository {

    fun reserveStock(orderId: String, itemIds: Collection<Long>): Boolean
    fun alreadyReserve(orderId: String): Boolean
}