package me.golf.core.repository.domain.stock

interface StockRepository {

    fun reserveStock(orderId: String, itemIds: Collection<Long>): Boolean
    fun alreadyReserveByTicketIds(orderId: String, ticketIds: List<Long>): Boolean
    fun existsReserveByOrderId(orderId: String): Boolean
    fun updateTtl(orderId: String)
}