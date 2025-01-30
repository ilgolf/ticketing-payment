package me.golf

import java.math.BigDecimal
import java.math.RoundingMode

fun calculate(cashBuy: BigDecimal, standardValue: BigDecimal, ratio: BigDecimal, multiplier: BigDecimal): BigDecimal {
    return (((cashBuy.subtract(standardValue)).multiply(ratio)).add(standardValue))
        .divide(multiplier, 4, RoundingMode.HALF_UP)
}

fun main() {
    // vpn
    val cashBuy = BigDecimal("9.697016949152543")
    val standardValue = BigDecimal("8.82")
    val multiplier = BigDecimal("100")
    val ratio = BigDecimal("0.04")

    val result = calculate(cashBuy, standardValue, ratio, multiplier)

    println(result)

    println(BigDecimal(1_000_000).divide(result, 0, RoundingMode.HALF_UP).toString())

    // 시중
    val ogCashBuy = BigDecimal("9.72")
    val ogStandardValue = BigDecimal("8.84")
    val ogMultiplier = BigDecimal("100")
    val ogRatio = BigDecimal("0.04")

    val ogResult = calculate(ogCashBuy, ogStandardValue, ogRatio, ogMultiplier)

    println(BigDecimal(1_000_000).divide(ogResult, 0, RoundingMode.HALF_UP).toString())
}