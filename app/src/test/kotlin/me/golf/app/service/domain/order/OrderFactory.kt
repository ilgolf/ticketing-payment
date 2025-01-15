package me.golf.app.service.domain.order

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import me.golf.core.model.domain.order.Order
import me.golf.core.model.domain.order.OrderMutator

object OrderFactory {

    fun createOrder(): Order {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        return fixtureMonkey
            .giveMeBuilder<OrderMutator>()
            .sample()
    }
}