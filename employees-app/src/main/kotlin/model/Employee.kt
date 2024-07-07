package com.example.model

import org.ktorm.entity.Entity
import java.util.*

interface Employee: Entity<Employee>, BaseEntity {
    companion object : Entity.Factory<Employee>()
    val id: UUID
    val name: String
    val position: String
    val email: String
    val salary: Money
}
