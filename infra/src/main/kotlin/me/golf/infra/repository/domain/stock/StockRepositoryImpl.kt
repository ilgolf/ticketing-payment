package me.golf.infra.repository.domain.stock

import me.golf.core.repository.domain.stock.StockRepository
import me.golf.infra.dao.domain.stock.StockDao
import org.springframework.stereotype.Repository

@Repository
class StockRepositoryImpl(
    private val stockDao: StockDao
): StockRepository {

    override fun reserveStock(orderId: String, itemIds: Collection<Long>): Boolean {
        return stockDao.saveOrderReserveInfo(orderId, itemIds)
    }

    override fun alreadyReserve(orderId: String): Boolean {
        return stockDao.existsReserveInfoByOrderId(orderId)
    }

    override fun alreadyReserveByTicketIds(ticketIds: List<Long>): Boolean {
        return true
    }
}