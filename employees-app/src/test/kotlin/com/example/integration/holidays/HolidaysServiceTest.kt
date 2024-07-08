package com.example.integration.holidays

import com.example.model.Country
import com.example.model.UAE
import com.example.model.UK
import com.example.model.aHoliday
import com.neovisionaries.i18n.CountryCode
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Instant
import java.time.temporal.ChronoUnit
import kotlin.test.assertEquals

class HolidaysServiceTest {
    private val openHolidaysAPI = mock<OpenHolidaysAPI>()
    private val clock = mock<Clock>()
    private val holidaysService = HolidaysService(openHolidaysAPI, clock)

    @Test
    fun `getUpcomingHolidays caches supported countries and holidays for 1 day`() {
        // given
        given(openHolidaysAPI.getSupportedCountries()).willReturn(listOf(UAE))
        val initialTime = Instant.now()
        val laterTime = initialTime
            .plus(1L, ChronoUnit.DAYS)
            .minus(1L, ChronoUnit.SECONDS) // should almost expire
        given(clock.millis()).willReturn(initialTime.toEpochMilli())
        holidaysService.getUpcomingHolidays(UAE)
        given(clock.millis()).willReturn(laterTime.toEpochMilli())

        // when
        holidaysService.getUpcomingHolidays(UAE)

        // then
        verify(openHolidaysAPI, times(1)).getSupportedCountries()
        verify(openHolidaysAPI, times(1)).getHolidays(UAE, 7)
    }

    @Test
    fun `getUpcomingHolidays caches expire after 1 day`() {
        // given
        given(openHolidaysAPI.getSupportedCountries()).willReturn(listOf(UAE))
        val initialTime = Instant.now()
        val laterTime = initialTime.plus(1L, ChronoUnit.DAYS)
        given(clock.millis()).willReturn(initialTime.toEpochMilli())
        holidaysService.getUpcomingHolidays(UAE)
        given(clock.millis()).willReturn(laterTime.toEpochMilli())

        // when
        holidaysService.getUpcomingHolidays(UAE)

        // then
        verify(openHolidaysAPI, times(2)).getSupportedCountries()
        verify(openHolidaysAPI, times(2)).getHolidays(UAE, 7)
    }

    @Test
    fun `getUpcomingHolidays throws CountryNotSupportedException if country is not supported`() {
        // given
        given(openHolidaysAPI.getSupportedCountries()).willReturn(listOf(UK))

        // when, then
        assertThrows<CountryNotSupportedException> {
            holidaysService.getUpcomingHolidays(UAE)
        }
    }

    @Test
    fun `getUpcomingHolidays returns holidays if country is supported`() {
        // given
        given(openHolidaysAPI.getSupportedCountries()).willReturn(listOf(UK))
        val expectedHolidays = listOf(aHoliday(), aHoliday())
        given(openHolidaysAPI.getHolidays(UK, 7)).willReturn(expectedHolidays)

        // when
        val actualHolidays = holidaysService.getUpcomingHolidays(UK)

        // then
        assertEquals(expectedHolidays, actualHolidays)
    }
}
