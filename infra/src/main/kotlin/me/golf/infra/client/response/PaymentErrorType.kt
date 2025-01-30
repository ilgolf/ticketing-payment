package me.golf.infra.client.response

enum class PaymentErrorType(val errorCode: String, val message: String) {
    // 일반적인 요청 오류
    INVALID_REQUEST("INVALID_REQUEST", "잘못된 요청입니다. 요청 정보를 확인해주세요."),
    UNAUTHORIZED("UNAUTHORIZED", "인증에 실패했습니다. API 키를 확인해주세요."),
    FORBIDDEN("FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND("NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    METHOD_NOT_ALLOWED("METHOD_NOT_ALLOWED", "허용되지 않은 HTTP 메서드입니다."),
    NOT_ACCEPTABLE("NOT_ACCEPTABLE", "요청한 리소스가 허용되지 않는 형식입니다."),
    UNSUPPORTED_MEDIA_TYPE("UNSUPPORTED_MEDIA_TYPE", "지원하지 않는 미디어 형식입니다."),

    // 결제 승인 관련 오류
    PAYMENT_FAILED("PAYMENT_FAILED", "결제 승인이 실패했습니다. 다시 시도해주세요."),
    INVALID_PAYMENT_METHOD("INVALID_PAYMENT_METHOD", "결제 수단이 유효하지 않습니다."),
    PAYMENT_DECLINED("PAYMENT_DECLINED", "결제가 거절되었습니다."),
    PAYMENT_PROCESSING_ERROR("PAYMENT_PROCESSING_ERROR", "결제 처리 중 오류가 발생했습니다."),
    INSUFFICIENT_BALANCE("INSUFFICIENT_BALANCE", "잔액이 부족합니다."),
    DUPLICATE_TRANSACTION("DUPLICATE_TRANSACTION", "중복된 거래 요청입니다."),
    EXPIRED_PAYMENT_KEY("EXPIRED_PAYMENT_KEY", "결제 키가 만료되었습니다."),
    INVALID_PAYMENT_KEY("INVALID_PAYMENT_KEY", "결제 키가 유효하지 않습니다."),

    // 서버 오류
    INTERNAL_SERVER_ERROR("INTERNAL_SERVER_ERROR", "서버에서 문제가 발생했습니다. 잠시 후 다시 시도해주세요."),
    SERVICE_UNAVAILABLE("SERVICE_UNAVAILABLE", "현재 서비스가 불가능합니다. 잠시 후 다시 시도해주세요."),
    TIMEOUT("TIMEOUT", "요청이 시간 초과되었습니다. 다시 시도해주세요."),

    // 기타
    UNKNOWN_ERROR("UNKNOWN_ERROR", "알 수 없는 오류가 발생했습니다.")
}
