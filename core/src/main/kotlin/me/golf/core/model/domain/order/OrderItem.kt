package me.golf.core.model.domain.order

interface OrderItem {

    /**
     * 주문 티켓 ID
     */
    val orderItemId: Long?

    /**
     * 주문 ID
     */
    val orderId : String

    /**
     * 상품 ID
     */
    val itemId : Long

    companion object {
        fun create(
            id: Long? = null,
            orderId: String,
            ticketId: Long,
        ) = OrderItemMutator(
            orderItemId =  id,
            orderId = orderId,
            itemId = ticketId,
        )
    }
}

class OrderItemMutator(
    override val orderItemId: Long?,
    override val orderId: String,
    override val itemId: Long,
): OrderItem
