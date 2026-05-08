package com.fintrack.domain.model

import java.math.BigDecimal
import java.math.RoundingMode

@JvmInline
value class Money(val minorUnits: Long) : Comparable<Money> {
    override fun compareTo(other: Money): Int = minorUnits.compareTo(other.minorUnits)

    operator fun plus(other: Money): Money = Money(minorUnits + other.minorUnits)

    operator fun minus(other: Money): Money = Money(minorUnits - other.minorUnits)

    operator fun unaryMinus(): Money = Money(-minorUnits)

    fun format(currencySymbol: String = "$"): String {
        val absolute = kotlin.math.abs(minorUnits)
        val whole = absolute / 100
        val cents = absolute % 100
        val sign = if (minorUnits < 0) "-" else ""
        return "$sign$currencySymbol$whole.${cents.toString().padStart(2, '0')}"
    }

    companion object {
        val ZERO = Money(0)

        fun parse(input: String): Result<Money> {
            val normalized = input.trim()
            if (normalized.isEmpty()) {
                return Result.failure(IllegalArgumentException("Amount is required"))
            }

            return runCatching {
                val decimal = BigDecimal(normalized)
                require(decimal.scale() <= 2) { "Amount can include at most 2 decimal places" }
                val minorUnits = decimal
                    .movePointRight(2)
                    .setScale(0, RoundingMode.UNNECESSARY)
                    .longValueExact()
                require(minorUnits > 0) { "Amount must be greater than 0" }
                Money(minorUnits)
            }
        }
    }
}
