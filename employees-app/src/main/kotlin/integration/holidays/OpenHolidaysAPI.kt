package com.example.integration.holidays

import com.example.model.Country
import com.example.model.Holiday
import com.neovisionaries.i18n.CountryCode
import io.ktor.client.*
import io.ktor.client.call.*
import io.ktor.client.request.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.Serializable
import java.text.SimpleDateFormat
import java.time.Clock
import java.time.LocalDate

private const val GET_SUPPORTED_COUNTRIES_URL = "https://openholidaysapi.org/Countries"
private const val GET_HOLIDAYS_URL = "https://openholidaysapi.org/PublicHolidays?countryIsoCode=%s&languageIsoCode=EN&validFrom=%s&validTo=%s"
private val DATE_FORMAT = SimpleDateFormat("yyyy-MM-dd")

class OpenHolidaysAPI(private val client: HttpClient, private val clock: Clock) {

    fun getSupportedCountries(): List<Country> = runBlocking {
        client.get(GET_SUPPORTED_COUNTRIES_URL).body<List<CountryResponse>>()
            .map { Country(it.isoCode) }
    }

    fun getHolidays(country: Country, checkPeriodDays: Int): List<Holiday> = runBlocking {
        val fromDate = LocalDate.now(clock)
        val toDate = fromDate.plusDays(checkPeriodDays.toLong())
        val url = GET_HOLIDAYS_URL.format(
            country.code.alpha2,
//            fromDate.toString(),
//            toDate.toString(),
            "2022-01-01",
            "2022-01-10"
        )
        client.get(url).body<List<HolidayResponse>>().map { response ->
            Holiday(
                englishDescription = response.name.first().text,
                startDate = DATE_FORMAT.parse(response.startDate),
                endDate = DATE_FORMAT.parse(response.endDate))
        }
    }
}

@Serializable
private class CountryResponse(val isoCode: CountryCode)

@Serializable
private class HolidayResponse(val startDate: String, val endDate: String, val name: List<LocalisedName>)

@Serializable
private class LocalisedName(val text: String)
