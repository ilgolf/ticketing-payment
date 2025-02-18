package me.golf.app.service.domain.stock.listener

import me.golf.core.repository.domain.stock.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StockListener(
    private val stockRepository: StockRepository,
) {

    private val log = LoggerFactory.getLogger(StockListener::class.java)

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCompleteEvent(event: OrderCompleteEvent) {
        log.info("선점 정보 저장 시작 : 주문 ID : {}", event.orderId)
        val result = kotlin.runCatching { stockRepository.reserveStock(orderId = event.orderId, itemIds = event.ticketIds) }

        result.onFailure {
            log.error("주문 ID : {}, 선점 실패 사유 : {}", event.orderId, it.message)
        }
    }
}

data class OrderCompleteEvent(val orderId: String, val ticketIds: List<Long>)