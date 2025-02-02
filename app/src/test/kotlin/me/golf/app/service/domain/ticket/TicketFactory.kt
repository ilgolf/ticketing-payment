package me.golf.app.service.domain.ticket

import com.navercorp.fixturemonkey.FixtureMonkey
import com.navercorp.fixturemonkey.kotlin.KotlinPlugin
import com.navercorp.fixturemonkey.kotlin.giveMeBuilder
import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.model.domain.ticket.TicketMutator
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.core.model.domain.ticket.seat.SeatMutator
import java.math.BigDecimal
import java.time.LocalDateTime

object TicketFactory {

    fun createTicket(
        id: Long? = null,
        status: TicketStatus? = null,
        price: BigDecimal = BigDecimal.valueOf(240000),
        openDateTime: LocalDateTime? = null,
        createdAt: LocalDateTime? = null,
        lastModifiedAt: LocalDateTime? = null,
    ): Ticket {
        val fixtureMonkey = FixtureMonkey.builder()
            .plugin(KotlinPlugin())
            .build()

        val seat = fixtureMonkey.giveMeBuilder<SeatMutator>()
            .set("row", 1)
            .set("number", 3)
            .sample()

        return fixtureMonkey
            .giveMeBuilder<TicketMutator>()
            .set("seat", seat)
            .also { builder -> id?.let { builder.set("id", it) } }
            .also { builder -> status?.let { builder.set("status", it) } }
            .set("price", price)
            .also { builder -> openDateTime?.let { builder.set("openDateTime", it) } }
            .also { builder -> createdAt?.let { builder.set("createdAt", it) } }
            .also { builder -> lastModifiedAt?.let { builder.set("lastModifiedAt", it) } }
            .sample()
    }
}