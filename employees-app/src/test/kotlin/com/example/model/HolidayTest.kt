package com.example.model

import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import java.text.SimpleDateFormat

class HolidayTest {
    @Test
    fun `constructor throws IllegalArgumentException when description is blank`() {
        assertThrows<IllegalArgumentException> {
            aHoliday(englishDescription = "  ")
        }
    }

    @Test
    fun `constructor throws IllegalArgumentException when startDate is after the endDate`() {
        assertThrows<IllegalArgumentException> {
            aHoliday(
                startDate = SimpleDateFormat("yyyy-MM-dd").parse("2023-01-02"),
                endDate = SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01")
            )
        }
    }
}
