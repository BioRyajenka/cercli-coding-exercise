package com.example.model

import com.neovisionaries.i18n.CountryCode
import java.text.SimpleDateFormat
import java.time.Instant
import java.util.*

fun anEmployee(
    id: UUID = UUID.randomUUID(),
    name: String = "John Doe",
    position: String = "Developer",
    email: String = "john.doe@example.com",
    salary: Money = Money.fromDouble(400.0),
    createdAt: Instant = Instant.ofEpochSecond(1720380242),
    modifiedAt: Instant = Instant.ofEpochSecond(1720380242),
    countryOfEmployment: Country = Country(CountryCode.UNDEFINED),
) = Employee(id, name, position, email, salary, countryOfEmployment, createdAt, modifiedAt)

fun aHoliday(
    englishDescription: String = "New Year's Day",
    startDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"),
    endDate: Date = SimpleDateFormat("yyyy-MM-dd").parse("2023-01-01"),
) = Holiday(englishDescription, startDate, endDate)
