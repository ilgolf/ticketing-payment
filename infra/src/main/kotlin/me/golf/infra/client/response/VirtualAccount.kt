package me.golf.infra.client.response

data class VirtualAccount(
    val accountType: String?,           // "일반" or "고정"
    val accountNumber: String?,         // max length: 20
    val bankCode: String?,              // 2-digit bank code
    val customerName: String?,          // max length: 100
    val dueDate: String?,              // ISO 8601 format: yyyy-MM-dd'T'HH:mm:ss
    val expired: Boolean?,
)
