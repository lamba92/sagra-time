package it.sagratime.core.units

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface LengthUnit {
    val factorToMeters: Double // Factor to convert this unit to meters

    @Serializable
    data object Millimeters : LengthUnit {
        override val factorToMeters: Double = 0.001

        override fun toString(): String = "mm"
    }

    @Serializable
    data object Centimeters : LengthUnit {
        override val factorToMeters: Double = 0.01

        override fun toString(): String = "cm"
    }

    @Serializable
    data object Meters : LengthUnit {
        override val factorToMeters: Double = 1.0

        override fun toString(): String = "m"
    }

    @Serializable
    data object Kilometers : LengthUnit {
        override val factorToMeters: Double = 1000.0

        override fun toString(): String = "km"
    }

    @Serializable
    data object Inches : LengthUnit {
        override val factorToMeters: Double = 0.0254

        override fun toString(): String = "in"
    }

    @Serializable
    data object Feet : LengthUnit {
        override val factorToMeters: Double = 0.3048

        override fun toString(): String = "ft"
    }

    @Serializable
    data object Yards : LengthUnit {
        override val factorToMeters: Double = 0.9144

        override fun toString(): String = "yd"
    }

    @Serializable
    data object Miles : LengthUnit {
        override val factorToMeters: Double = 1609.34

        override fun toString(): String = "mi"
    }
}

@JvmInline
@Serializable
value class Length(
    private val meters: Double,
) : Comparable<Length> {
    companion object {
        fun from(
            value: Number,
            unit: LengthUnit,
        ) = Length(value.toDouble() * unit.factorToMeters)

        val ZERO = Length(0.0)
    }

    val inMillimeters: Double get() = meters / LengthUnit.Millimeters.factorToMeters
    val inCentimeters: Double get() = meters / LengthUnit.Centimeters.factorToMeters
    val inMeters: Double get() = meters
    val inKilometers: Double get() = meters / LengthUnit.Kilometers.factorToMeters
    val inInches: Double get() = meters / LengthUnit.Inches.factorToMeters
    val inFeet: Double get() = meters / LengthUnit.Feet.factorToMeters
    val inYards: Double get() = meters / LengthUnit.Yards.factorToMeters
    val inMiles: Double get() = meters / LengthUnit.Miles.factorToMeters

    fun to(unit: LengthUnit): Double = meters / unit.factorToMeters

    operator fun plus(other: Length): Length = Length.from(meters + other.meters, LengthUnit.Meters)

    operator fun minus(other: Length): Length = Length.from(meters - other.meters, LengthUnit.Meters)

    operator fun times(factor: Number): Length = Length.from(meters * factor.toDouble(), LengthUnit.Meters)

    operator fun div(factor: Number): Length = Length.from(meters / factor.toDouble(), LengthUnit.Meters)

    override fun compareTo(other: Length): Int = this.meters.compareTo(other.meters)

    override fun toString(): String =
        when {
            meters < LengthUnit.Meters.factorToMeters -> "$inMillimeters ${LengthUnit.Millimeters}"
            meters < LengthUnit.Kilometers.factorToMeters -> "$inCentimeters ${LengthUnit.Centimeters}"
            meters < LengthUnit.Miles.factorToMeters -> "$inMeters ${LengthUnit.Meters}"
            else -> "$inKilometers ${LengthUnit.Kilometers}"
        }

    fun toString(
        unit: LengthUnit,
        precision: Int = 2,
    ): String =
        when (unit) {
            LengthUnit.Millimeters -> "${inMillimeters.toStringWithPrecision(precision)} ${LengthUnit.Millimeters}"
            LengthUnit.Centimeters -> "${inCentimeters.toStringWithPrecision(precision)} ${LengthUnit.Centimeters}"
            LengthUnit.Meters -> "${inMeters.toStringWithPrecision(precision)} ${LengthUnit.Meters}"
            LengthUnit.Kilometers -> "${inKilometers.toStringWithPrecision(precision)} ${LengthUnit.Kilometers}"
            LengthUnit.Inches -> "${inInches.toStringWithPrecision(precision)} ${LengthUnit.Inches}"
            LengthUnit.Feet -> "${inFeet.toStringWithPrecision(precision)} ${LengthUnit.Feet}"
            LengthUnit.Yards -> "${inYards.toStringWithPrecision(precision)} ${LengthUnit.Yards}"
            LengthUnit.Miles -> "${inMiles.toStringWithPrecision(precision)} ${LengthUnit.Miles}"
        }
}

val Number.millimeters: Length get() = Length.from(this, LengthUnit.Millimeters)
val Number.centimeters: Length get() = Length.from(this, LengthUnit.Centimeters)
val Number.meters: Length get() = Length.from(this, LengthUnit.Meters)
val Number.kilometers: Length get() = Length.from(this, LengthUnit.Kilometers)
val Number.inches: Length get() = Length.from(this, LengthUnit.Inches)
val Number.feet: Length get() = Length.from(this, LengthUnit.Feet)
val Number.yards: Length get() = Length.from(this, LengthUnit.Yards)
val Number.miles: Length get() = Length.from(this, LengthUnit.Miles)
