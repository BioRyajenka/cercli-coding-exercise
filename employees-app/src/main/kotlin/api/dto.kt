package com.example.api

import com.example.model.Country
import com.example.model.Email
import com.example.util.InstantSerializer
import com.example.util.LocalDateTimeSerializer
import com.example.util.UUIDSerializer
import kotlinx.serialization.Serializable
import java.time.Instant
import java.time.LocalDateTime
import java.util.UUID

@Serializable
data class CreateEmployeeRequest(
    val name: String,
    val position: String,
    val email: Email,
    val salary: Double,
    val countryOfEmployment: Country,
)

@Serializable
data class UpdateEmployeeRequest(
    val name: String? = null,
    val position: String? = null,
    val email: Email? = null,
    val salary: Double? = null,
    val countryOfEmployment: Country? = null,
)

@Serializable
data class EmployeeResponse(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val position: String,
    val email: Email,
    val salary: Double,
    val countryOfEmployment: Country,
    @Serializable(with = LocalDateTimeSerializer::class)
    val createdAtLocal: LocalDateTime,
    @Serializable(with = LocalDateTimeSerializer::class)
    val modifiedAtLocal: LocalDateTime,
)
