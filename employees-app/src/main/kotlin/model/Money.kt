package com.example.model

import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

/**
 * Money is represented as "long" to get around potential precision issues
 */
class Money(val minorAmount: Long) {
    fun toDouble(): Double = minorAmount / 100.0
    companion object {
        fun fromDouble(double: Double): Money {
            val (integer, fractional) = double.toString().split(".").map(String::toInt)
            require(fractional < 100) {
                "Cannot convert $double to money"
            }
            return Money(integer * 100L + fractional)
        }
    }
}

object MoneySerializer : KSerializer<Money> {
    override val descriptor = PrimitiveSerialDescriptor("Money", PrimitiveKind.DOUBLE)

    override fun deserialize(decoder: Decoder) = Money.fromDouble(decoder.decodeDouble())

    override fun serialize(encoder: Encoder, value: Money) = encoder.encodeDouble(value.toDouble())

}
