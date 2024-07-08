package com.example.model

import java.util.Date

data class Holiday(val englishDescription: String, val startDate: Date, val endDate: Date) {
    init {
        require(englishDescription.isNotBlank())
        require(startDate <= endDate)
    }
}
