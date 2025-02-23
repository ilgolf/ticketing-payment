package me.golf.core.repository.domain.stock

interface StockRepository {

    fun reserveStock(orderId: String, itemIds: Collection<Long>): Boolean
    fun alreadyReserveByTicketIds(orderId: String, ticketIds: List<Long>): Boolean
    fun existsReserveByOrderId(orderId: String): Boolean
    fun alreadyReserveByTicketIds(ticketIds: Collection<Long>): Boolean
    fun updateTtl(orderId: String)
    fun cancelReserve(orderId: String)
}