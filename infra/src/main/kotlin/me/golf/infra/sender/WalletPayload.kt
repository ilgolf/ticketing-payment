package me.golf.infra.sender

data class WalletPayload(
    val paymentId: Long,
    val userId: Long
) {
    fun toJson(): String = """
            {
                "paymentId": $paymentId,
                "userId": $userId
            }
        """.trimIndent()
}