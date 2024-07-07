package com.example.model

import java.time.Instant

interface BaseEntity {
    val createdAt: Instant
    val modifiedAt: Instant
}
