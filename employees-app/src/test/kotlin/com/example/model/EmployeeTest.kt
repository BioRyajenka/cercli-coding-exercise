package com.example.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class EmployeeTest {
    @Test
    fun `constructor throws IllegalArgumentException when name is blank`() {
        assertThrows<IllegalArgumentException> {
            anEmployee(name = "   ")
        }
    }

    @Test
    fun `constructor throws IllegalArgumentException when position is blank`() {
        assertThrows<IllegalArgumentException> {
            anEmployee(position = "   ")
        }
    }

    @Test
    fun `constructor throws IllegalArgumentException when email is invalid`() {
        assertThrows<IllegalArgumentException> {
            anEmployee(email = "invalid@email")
        }
    }
}
