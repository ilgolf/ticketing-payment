package me.golf.core.model.domain.ticket

import me.golf.core.model.domain.ticket.seat.Seat
import java.math.BigDecimal
import java.time.LocalDateTime
import java.util.*

interface Ticket {

    /**
     * 티켓 식별자
     */
    val id: Long

    /**
     * 좌석 정보
     */
    val seat: Seat

    /**
     * 티켓 상태
     */
    val status: TicketStatus

    /**
     * 티켓 가격
     */
    val price: BigDecimal

    /**
     * 티켓 구매 오픈시간
     */
    val openDateTime: LocalDateTime

    /**
     * 티켓 생성일
     */
    val createdAt: LocalDateTime

    /**
     * 티켓 정보 마지막 수정일
     */
    val lastModifiedAt: LocalDateTime

    fun purchase(): Ticket
    fun cancel(): Ticket
    fun open(): Ticket
    fun isNonPurchase(): Boolean

    fun mutate() = TicketMutator(
        id = this.id,
        seat = this.seat,
        status = this.status,
        price = this.price,
        openDateTime = this.openDateTime,
        createdAt = this.createdAt,
        lastModifiedAt = this.lastModifiedAt,
    )

    companion object {
        fun create(
            id: Long,
            seat: Seat,
            status: TicketStatus,
            price: BigDecimal,
            openDateTime: LocalDateTime,
            createdAt: LocalDateTime,
            lastModifiedAt: LocalDateTime,
        ): Ticket {
            return TicketMutator(
                id = id,
                seat = seat,
                status = status,
                price = price,
                openDateTime = openDateTime,
                createdAt = createdAt,
                lastModifiedAt = lastModifiedAt,
            )
        }
    }
}

class TicketMutator(
    override val id: Long,
    override val seat: Seat,
    override val status: TicketStatus,
    override val price: BigDecimal,
    override val openDateTime: LocalDateTime,
    override val createdAt: LocalDateTime,
    override val lastModifiedAt: LocalDateTime
) : Ticket {

    init {
        require(price > BigDecimal.ZERO) { "가격은 최소 0원 부터 시작합니다." }
    }

    override fun purchase(): Ticket {
        if (!isPurchasable()) {
            throw IllegalArgumentException("이미 구매되었거나 구매 불가능한 티켓입니다.")
        }

        this.seat.reserve()

        return TicketMutator(
            this.id,
            this.seat,
            TicketStatus.SOLD,
            this.price,
            this.createdAt,
            this.openDateTime,
            this.lastModifiedAt
        )
    }

    override fun cancel(): Ticket {
        if (!isCancelable()) {
            throw IllegalArgumentException("이미 취소된 상품이거나 판매중인 티켓이 아닙니다.")
        }

        this.seat.release()

        return TicketMutator(
            this.id,
            this.seat,
            TicketStatus.AVAILABLE,
            this.price,
            this.createdAt,
            this.openDateTime,
            this.lastModifiedAt
        )
    }

    override fun open(): Ticket {
        if (this.status !== TicketStatus.DISABLED) {
            throw IllegalArgumentException("")
        }

        return TicketMutator(
            this.id,
            this.seat,
            TicketStatus.AVAILABLE,
            this.price,
            this.createdAt,
            this.openDateTime,
            this.lastModifiedAt
        )
    }

    override fun isNonPurchase(): Boolean {
        return this.status === TicketStatus.SOLD || this.status === TicketStatus.DISABLED
    }

    private fun isPurchasable(): Boolean {
        val ticketStatuses = EnumSet.of(TicketStatus.AVAILABLE)
        return this.status in ticketStatuses
    }

    private fun isCancelable(): Boolean {
        val ticketStatuses = EnumSet.of(TicketStatus.SOLD)
        return this.status in ticketStatuses
    }
}


