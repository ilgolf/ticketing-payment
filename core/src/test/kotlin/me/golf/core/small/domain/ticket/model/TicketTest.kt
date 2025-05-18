package me.golf.core.small.domain.ticket.model

import io.kotest.matchers.collections.shouldBeIn
import io.kotest.matchers.shouldBe
import io.kotest.matchers.types.shouldBeSameInstanceAs
import me.golf.core.model.domain.ticket.Ticket
import me.golf.core.model.domain.ticket.TicketStatus
import me.golf.core.model.domain.ticket.seat.Seat
import me.golf.core.small.domain.ticket.TicketFactory
import me.golf.core.small.domain.ticket.seat.SeatFactory
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.*
import java.math.BigDecimal

class TicketTest {

    private lateinit var seat: Seat
    private lateinit var ticket: Ticket

    @BeforeEach
    fun setUp() {
        seat = SeatFactory.create()
        ticket = TicketFactory.create(seat = seat)
    }

    @Test
    @DisplayName("티켓을 구입 합니다. 이 때 좌석도 예약 상태로 변경됩니다.")
    fun test1() {
        // given
        val expectedTicketStatus = TicketStatus.SOLD
        val expectedSeatStatus = false

        // when
        val purchasedTicket = ticket.purchase()

        // then
        assertAll(
            { purchasedTicket.status shouldBe expectedTicketStatus },
            { purchasedTicket.seat.isAvailable shouldBe expectedSeatStatus },
        )
    }

    @Test
    @DisplayName("티켓 구매를 취소합니다. 이 때 좌석은 다시 예약 가능해야합니다.")
    fun test2() {
        // given
        val purchaseTicket = ticket.purchase()

        val expectedTicketStatus = TicketStatus.AVAILABLE
        val expectedSeatStatus = true

        // when
        val cancelTicket = purchaseTicket.cancel()

        // then
        assertAll(
            { cancelTicket.status shouldBe expectedTicketStatus },
            { cancelTicket.seat.isAvailable shouldBe expectedSeatStatus },
        )
    }

    @Test
    @DisplayName("티켓을 오픈 합니다.")
    fun test3() {
        // given
        val unOpenTicket = TicketFactory.create(status = TicketStatus.DISABLED, seat = seat)
        val expectedTicketStatus = TicketStatus.AVAILABLE

        // when
        val openTicket = unOpenTicket.open()

        // then
        openTicket.status shouldBe expectedTicketStatus
    }

    @Test
    @DisplayName("구입 할 수 없으면 true 를 반환합니다.")
    fun test4() {
        // given
        val givenTicket = TicketFactory.create(status = TicketStatus.DISABLED, seat = seat)
        val expected = true

        // when
        val result = givenTicket.isNonPurchase()

        // then
        result shouldBe expected
    }

    @Test
    @DisplayName("구입할 수 있으면 false 를 반환합니다.")
    fun test5() {
        // given
        val givenTicket = TicketFactory.create(status = TicketStatus.AVAILABLE, seat = seat)
        val expected = false

        // when
        val result = givenTicket.isNonPurchase()

        // then
        result shouldBe expected
    }

    @Test
    @DisplayName("가격은 0원 이상 부터 매길 수 있습니다.")
    fun test7() {
        // given
        val givenPrice = BigDecimal.valueOf(-1000)

        // when
        val result = catchThrowable { TicketFactory.create(price = givenPrice, seat = seat) }

        // then
        assertThat(result).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("티켓이 구입할 수 없는 상태면 구입에 실패해야합니다.")
    fun test8() {
        // given
        val givenTicket = TicketFactory.create(status = TicketStatus.DISABLED, seat = seat)

        // when
        val result = catchThrowable { givenTicket.purchase() }

        // then
        assertThat(result).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("취소할 수 없는 상태면 티켓을 취소하는데 실패합니다.")
    fun test9() {
        // given
        val givenTicket = TicketFactory.create(status = TicketStatus.AVAILABLE, seat = seat)

        // when
        val result = catchThrowable { givenTicket.cancel() }

        // then
        assertThat(result).isInstanceOf(IllegalArgumentException::class.java)
    }

    @Test
    @DisplayName("이미 오픈 하거나 팔린 상태이면 티켓을 오픈할 수 없습니다.")
    fun test10() {
        // given
        val givenTicket = TicketFactory.create(status = TicketStatus.SOLD, seat = seat)

        // when
        val result = catchThrowable { givenTicket.open() }

        // then
        assertThat(result).isInstanceOf(IllegalArgumentException::class.java)
    }
}