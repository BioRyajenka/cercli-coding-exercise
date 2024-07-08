package com.example.integration.holidays

import com.example.model.Country
import com.example.util.SingleValueCache
import com.github.benmanes.caffeine.cache.Caffeine
import com.example.model.Holiday
import java.time.Clock
import java.util.concurrent.TimeUnit
import kotlin.time.Duration.Companion.days
import kotlin.time.toJavaDuration

/**
 * https://www.openholidaysapi.org/en/
 *
 * note: this can be extracted to a separate module/project for reusability
 */
class HolidaysService(private val openHolidaysAPI: OpenHolidaysAPI, private val clock: Clock) {
    companion object {
        const val CHECK_PERIOD_DAYS = 7
    }

    private val holidays = Caffeine.newBuilder()
        .expireAfterWrite(1.days.toJavaDuration())
        .ticker { TimeUnit.MILLISECONDS.toNanos(clock.millis()) }
        .build { country: Country -> openHolidaysAPI.getHolidays(country, CHECK_PERIOD_DAYS) }

    private val supportedCountries = SingleValueCache(
        expireAfterWrite = 1.days,
        clock = clock,
    ) {
        openHolidaysAPI.getSupportedCountries()
    }

    fun getUpcomingHolidays(country: Country): List<Holiday> {
        if (country !in supportedCountries.get()) {
            throw CountryNotSupportedException(country)
        }
        return holidays.get(country)
    }
}

class CountryNotSupportedException(val country: Country): Exception()
