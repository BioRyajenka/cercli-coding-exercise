package com.example.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Money is represented as "long" to get around potential precision issues
 */
// TODO (out of scope for this exercise): add currency
data class Money(val minorAmount: Long) {
    fun toDouble(): Double = minorAmount / 100.0
    companion object {
        fun fromDouble(double: Double): Money {
            val stringRepresentation = double.toString()
            val (integerPart, fractionalPart) = stringRepresentation.split(".")
            require(fractionalPart.length <= 2) {
                "Cannot convert $double to money: too many fractional digits"
            }
            return Money(integerPart.toLong() * 100 + fractionalPart.padEnd(2, '0').toInt())
        }
    }
}

object MoneySerializer : KSerializer<Money> {
    override val descriptor = PrimitiveSerialDescriptor("Money", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder) = Money.fromDouble(decoder.decodeDouble())

    override fun serialize(encoder: Encoder, value: Money) = encoder.encodeDouble(value.toDouble())

}
