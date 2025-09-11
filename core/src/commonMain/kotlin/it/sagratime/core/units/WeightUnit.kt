package it.sagratime.core.units

import kotlinx.serialization.Serializable
import kotlin.jvm.JvmInline

@Serializable
sealed interface WeightUnit {
    val factorToGrams: Double

    @Serializable
    data object Grams : WeightUnit {
        override val factorToGrams: Double = 1.0

        override fun toString(): String = "g"
    }

    @Serializable
    data object Kilograms : WeightUnit {
        override val factorToGrams: Double = 1000.0

        override fun toString(): String = "kg"
    }

    @Serializable
    data object Pounds : WeightUnit {
        override val factorToGrams: Double = 453.59237

        override fun toString(): String = "lb"
    }

    @Serializable
    data object Ounces : WeightUnit {
        override val factorToGrams: Double = 28.349523125

        override fun toString(): String = "oz"
    }
}

@JvmInline
@Serializable
value class Weight(
    private val grams: Double,
) : Comparable<Weight> {
    companion object {
        fun from(
            value: Number,
            unit: WeightUnit,
        ) = Weight(value.toDouble() * unit.factorToGrams)

        val ZERO = Weight(0.0)
    }

    val inGrams: Double get() = grams
    val inKilograms: Double get() = grams / WeightUnit.Kilograms.factorToGrams
    val inPounds: Double get() = grams / WeightUnit.Pounds.factorToGrams
    val inOunces: Double get() = grams / WeightUnit.Ounces.factorToGrams

    fun to(unit: WeightUnit): Double = grams / unit.factorToGrams

    operator fun plus(other: Weight): Weight = from(grams + other.grams, WeightUnit.Grams)

    operator fun minus(other: Weight): Weight = from(grams - other.grams, WeightUnit.Grams)

    operator fun times(factor: Number): Weight = from(grams * factor.toDouble(), WeightUnit.Grams)

    operator fun div(factor: Number): Weight = from(grams / factor.toDouble(), WeightUnit.Grams)

    override fun compareTo(other: Weight): Int = grams.compareTo(other.grams)

    override fun toString(): String =
        when {
            inGrams < 1000 -> "${inGrams.toInt()} ${WeightUnit.Grams}"
            else -> "${inKilograms.toInt()} ${WeightUnit.Kilograms}"
        }

    fun toString(
        unit: WeightUnit,
        precision: Int = 2,
    ): String =
        when (unit) {
            WeightUnit.Grams -> inGrams.toStringWithPrecision(precision) + " ${WeightUnit.Grams}"
            WeightUnit.Kilograms -> inKilograms.toStringWithPrecision(precision) + " ${WeightUnit.Kilograms}"
            WeightUnit.Pounds -> inPounds.toStringWithPrecision(precision) + " ${WeightUnit.Pounds}"
            WeightUnit.Ounces -> inOunces.toStringWithPrecision(precision) + " ${WeightUnit.Ounces}"
        }
}

val Number.grams: Weight get() = Weight.from(this, WeightUnit.Grams)
val Number.kilograms: Weight get() = Weight.from(this, WeightUnit.Kilograms)
val Number.pounds: Weight get() = Weight.from(this, WeightUnit.Pounds)
val Number.ounces: Weight get() = Weight.from(this, WeightUnit.Ounces)
