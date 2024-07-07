package com.example.api

import com.example.model.Country
import kotlinx.serialization.Serializable

@Serializable
data class CreateEmployeeRequest(
    val name: String,
    val position: String,
    val email: String,
    val salary: Double,
    val countryOfEmployment: Country,
)

@Serializable
data class UpdateEmployeeRequest(
    val name: String? = null,
    val position: String? = null,
    val email: String? = null,
    val salary: Double? = null,
    val countryOfEmployment: Country? = null,
)
