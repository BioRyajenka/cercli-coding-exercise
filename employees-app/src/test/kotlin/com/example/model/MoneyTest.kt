package com.example.model

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class MoneyTest {
    @Test
    fun `fromDouble throws IllegalArgumentException when too much fractional digits provided`() {
        // given
        val double = 123.354

        // when, then
        assertThrows<IllegalArgumentException> {
            Money.fromDouble(double)
        }
    }

    @Test
    fun `fromDouble correctly converts double to money`() {
        // given
        val double = 123.5

        // when
        val actual = Money.fromDouble(double)

        // then
        assertEquals(12350, actual.minorAmount)
    }
}
