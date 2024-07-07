package com.example.api

import com.example.model.Employee
import javax.sql.DataSource

class EmployeeService(private val dbConnectionPool: DataSource) {
    fun create(createRequest: CreateEmployeeRequest): Employee {
        TODO()
    }
}
