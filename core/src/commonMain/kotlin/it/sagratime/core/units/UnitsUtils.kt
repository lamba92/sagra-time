package it.sagratime.core.units

import kotlin.math.pow

/**
 * Converts a [Number] to a string with exactly [precision] digits after the decimal point.
 *
 * This implementation is safe for Kotlin Multiplatform `commonMain`, and handles cases like:
 * - Adding trailing zeros to match the desired precision.
 * - Truncating fractional digits beyond the desired precision.
 * - Preserving exponential notation (e.g., "1.23E-5") by copying the rest of the string once such a symbol is encountered.
 *
 * Note: this method does **not round** — it truncates digits after the precision.
 *
 * ### Examples:
 * ```
 * 3.14159.toStringWithPrecision(2) // "3.14"
 * 3.1.toStringWithPrecision(3)     // "3.100"
 * 5.toStringWithPrecision(2)       // "5.00"
 * 1.0E-4.toStringWithPrecision(2)  // "1.0E-4"
 * ```
 *
 * @param precision the number of digits to keep after the decimal point.
 * @return a string representation of the number with the specified decimal precision.
 */
fun Number.toStringWithPrecision(precision: Int): String {
    require(precision >= 0) { "Precision must be non-negative" }

    val powerOf10 = 10.0.pow(precision.toDouble())
    val scaledNumber = (this.toDouble() * powerOf10).toLong()
    val integerPart = scaledNumber / powerOf10.toLong()
    val fractionalPart = (scaledNumber % powerOf10.toLong()).toString().padStart(precision, '0')
    return if (precision == 0) integerPart.toString() else "$integerPart.$fractionalPart"
}
