package com.example.api

import com.example.database.Employees
import com.example.model.Employee
import org.ktorm.database.Database
import org.ktorm.dsl.insert
import javax.sql.DataSource

class EmployeeRepository(dbConnectionPool: DataSource) {
    private val database = Database.connect(dbConnectionPool)

    fun save(employee: Employee): Employee {
        database.insert(Employees) {
            set(it.id, employee.id)
            set(it.name, employee.name)
            set(it.position, employee.position)
            set(it.email, employee.email)
            set(it.salary, employee.salary.minorAmount)
            set(it.created_at, employee.createdAt)
            set(it.modified_at, employee.modifiedAt)
        }
        return employee
    }
}
