package com.example.model

import kotlinx.serialization.Serializable

/**
 * Money is represented as "long" to get around potential precision issues
 */
class Money(val minorAmount: Long) {
    companion object {
        fun fromFloat(float: Float): Money {
            val (integer, fractional) = float.toString().split(".").map(String::toInt)
            require(fractional < 100) {
                "Cannot convert $float to money"
            }
            return Money(integer * 100L + fractional)
        }
    }
}
