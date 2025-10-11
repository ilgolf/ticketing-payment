package me.golf.app.service.domain.order

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.orderitem.OrderItem
import me.golf.core.model.domain.order.OrderMutator
import java.math.BigDecimal

object OrderFactory {

    fun createOrder(orderId: String = "20250223-0001", orderItem: OrderItem, amount: BigDecimal): Order {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        return fixtureMonkey
            .giveMeBuilder<OrderMutator>()
            .set("orderId", orderId)
            .set("orderItem", listOf(orderItem))
            .set("amount", amount)
            .sample()
    }
}