package me.golf.app.service.domain.order

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderItem
import me.golf.core.model.domain.order.OrderMutator

object OrderFactory {

    fun createOrder(orderItem: OrderItem): Order {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        return fixtureMonkey
            .giveMeBuilder<OrderMutator>()
            .set("orderItem", listOf(orderItem))
            .sample()
    }
}