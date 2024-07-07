package com.example.api

import kotlinx.serialization.Serializable

@Serializable
data class CreateEmployeeRequest(
    val name: String,
    val position: String,
    val email: String,
    val salary: Float,
)
