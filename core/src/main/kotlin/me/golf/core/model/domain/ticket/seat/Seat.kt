package me.golf.core.model.domain.ticket.seat

interface Seat {

    /**
     * 좌석 식별자
     */
    val id: Long

    /**
     * 콘서트 자리 열 번호
     */
    val row: Int

    /**
     * 콘서트 자리 층 수
     */
    val floor: Int

    /**
     * 콘서트 자리 번호 (col)
     */
    val number: Int

    /**
     * 콘서트 자리 구역
     */
    val section: SeatSection

    /**
     * 콘서트 자리 활성화 여부
     */
    val isAvailable: Boolean

    fun reserve(): Seat
    fun release(): Seat

    companion object {
        fun create(
            id: Long,
            row: Int,
            floor: Int,
            number: Int,
            section: SeatSection,
            isAvailable: Boolean,
        ) =  SeatMutator(
            id = id,
            row = row,
            floor = floor,
            number = number,
            section = section,
            isAvailable = isAvailable,
        )
    }
}

class SeatMutator(
    override val id: Long,
    override val row: Int,
    override val floor: Int,
    override val number: Int,
    override val section: SeatSection,
    override val isAvailable: Boolean,
): Seat {

    init {
        require(row > 0) { "Row must not be empty." }
        require(number > 0) { "Column must be greater than zero." }
    }

    override fun reserve(): Seat {
        if (!this.isAvailable) {
            throw IllegalArgumentException("")
        }

        return SeatMutator(
            this.id,
            this.row,
            this.floor,
            this.number,
            this.section,
            false,
        )
    }

    override fun release(): Seat {
        if (this.isAvailable) {
            throw IllegalArgumentException("")
        }

        return SeatMutator(
            this.id,
            this.row,
            this.floor,
            this.number,
            this.section,
            true,
        )
    }
}
