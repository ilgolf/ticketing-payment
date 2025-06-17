package me.golf.core.model.domain.payment.enumerate

// 2. 이벤트 상태 정의
enum class EventStatus {
    PENDING,    // 대기중
    PROCESSING, // 처리중
    COMPLETED,  // 완료
    FAILED      // 실패
}