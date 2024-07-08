package com.example.model

import org.junit.jupiter.api.Test

class EmailTest {
    @Test
    fun `constructor throws IllegalArgumentException when email is invalid`() {
        org.junit.jupiter.api.assertThrows<IllegalArgumentException> {
            Email("invalid@email")
        }
    }
}
