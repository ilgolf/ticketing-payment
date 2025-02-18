package me.golf.infra.repository.domain.stock

import me.golf.core.repository.domain.stock.StockRepository
import me.golf.infra.dao.domain.stock.StockDao
import org.springframework.stereotype.Repository

@Repository
class StockRepositoryImpl(
    private val stockDao: StockDao
): StockRepository {

    override fun reserveStock(orderId: String, itemIds: Collection<Long>): Boolean {
        return stockDao.saveOrderReserveInfo(orderId, itemIds, 20)
    }

    override fun alreadyReserveByTicketIds(orderId: String, ticketIds: List<Long>): Boolean {
        return stockDao.existsReserveInfoByOrderId(orderId, ticketIds)
    }

    override fun existsReserveByOrderId(orderId: String): Boolean {
        return stockDao.existsReserveByOrderId(orderId)
    }

    override fun updateTtl(orderId: String) {
        stockDao.updateTtl(orderId, 20)
    }
}