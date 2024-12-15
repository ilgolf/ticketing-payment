package me.golf.core.model.domain.order

interface OrderItem {

    /**
     * 주문 티켓 ID
     */
    val orderItemId: Long

    /**
     * 주문 ID
     */
    val orderId : Long

    /**
     * 상품 ID
     */
    val itemId : Long
}