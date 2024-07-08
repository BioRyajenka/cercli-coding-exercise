package com.example.api

import com.example.model.Country
import com.example.model.Email
import kotlinx.serialization.Serializable

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
