package me.golf.infra.client.response

import me.golf.infra.client.enum.EasyPayCorpCode
import java.math.BigDecimal

data class EasyPay(
    val provider: EasyPayCorpCode,
    val amount: BigDecimal,
    val discountAmount: BigDecimal,
)