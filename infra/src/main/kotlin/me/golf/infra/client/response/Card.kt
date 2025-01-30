package me.golf.infra.client.response

import java.math.BigDecimal

data class Card(
    val issuerCode: String,
    val acquirerCode: String,
    val number: String,
    val installmentPlanMonths: Int,
    val isInterestFree: Boolean,
    val interestPayer: String?,
    val approveNo: String,
    val useCardPoint: Boolean,
    val cardType: String,
    val ownerType: String,
    val acquireStatus: String,
    val receiptUrl: String,
    val amount: BigDecimal
)