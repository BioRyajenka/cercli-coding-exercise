package com.example.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateEmployeeRequest(
    val name: String,
    val position: String,
    val email: String,
    val salary: Double,
)

@Serializable
data class UpdateEmployeeRequest(
    val name: String? = null,
    val position: String? = null,
    val email: String? = null,
    val salary: Double? = null,
)
