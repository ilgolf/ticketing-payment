package me.golf.app.service.domain.stock.listener

import me.golf.app.service.domain.stock.listener.dto.CheckoutSuccessEvent
import me.golf.app.service.domain.stock.listener.dto.OrderCompleteEvent
import me.golf.app.service.domain.stock.listener.dto.OrderFailEvent
import me.golf.app.service.domain.stock.listener.dto.PaymentSuccessEvent
import me.golf.core.repository.domain.stock.StockRepository
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional
import org.springframework.transaction.event.TransactionPhase
import org.springframework.transaction.event.TransactionalEventListener

@Component
class StockListener(
    private val stockRepository: StockRepository,
) {

    private val log = LoggerFactory.getLogger(StockListener::class.java)

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleOrderCompleteEvent(event: OrderCompleteEvent) {
        log.info("선점 정보 저장 시작 : 주문 ID : {}", event.orderId)
        val result = kotlin.runCatching { stockRepository.reserveStock(orderId = event.orderId, itemIds = event.ticketIds) }

        result.onFailure {
            log.info("주문 ID : {}, 선점 실패 사유 : {}", event.orderId, it.message)
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handleStockReservation(event: CheckoutSuccessEvent) {
        log.info("선점 정보 갱신 시작 : 주문 ID : {}", event.orderId)

        kotlin.runCatching { updateStockReservationProcess(event) }
            .onFailure { log.info("선점 실패 : {}", event.orderId) }
    }

    private fun updateStockReservationProcess(event: CheckoutSuccessEvent) {
        if (stockRepository.existsReserveByOrderId(event.orderId)) {
            stockRepository.updateTtl(event.orderId)
            return
        }

        retryReserveStock(event.orderId, event.ticketIds)
    }

    private fun retryReserveStock(orderId: String, ticketIds: List<Long>) {
        val reserveResult = stockRepository.reserveStock(orderId, ticketIds)

        if (!reserveResult) {
            throw IllegalArgumentException("상품 선점에 실패했습니다.")
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    fun handlePaymentSuccessEvent(event: PaymentSuccessEvent) {
        log.info("결제 성공 선점 해제 시작: {}", event.orderId)
        val result = kotlin.runCatching { stockRepository.cancelReserve(event.orderId) }

        result.onFailure {
            log.info("주문 ID : {} 선점 종료 실패 사유 : {}", event.orderId, it.message)
        }
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener(phase = TransactionPhase.AFTER_ROLLBACK)
    fun handleOrderFailEvent(event: OrderFailEvent) {
        log.info("결제/checkout 실패 후 선점 롤백 시작 : {}", event.orderId)
        val result = kotlin.runCatching { stockRepository.cancelReserve(event.orderId) }

        result.onFailure {
            log.info("주문 ID : {} 선점 실패 사유: {}", event.orderId, it.message)
        }
    }
}
