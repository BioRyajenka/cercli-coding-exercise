package com.example.api

import com.example.database.Employees
import com.example.database.forUpdate
import com.example.model.Employee
import com.example.model.Money
import org.ktorm.database.Database
import org.ktorm.database.TransactionIsolation
import org.ktorm.database.iterator
import org.ktorm.dsl.*
import org.ktorm.entity.filter
import org.ktorm.entity.sequenceOf
import org.ktorm.expression.SelectExpression
import org.ktorm.expression.SqlExpression
import org.ktorm.expression.UnionExpression
import java.util.*
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

    fun update(id: UUID, update: (Employee) -> Employee) {
        database.useTransaction(isolation = TransactionIsolation.READ_COMMITTED) {
            // note: select for update is needed to prevent concurrent access to same row
            val row = database.from(Employees)
                .select()
                .where(Employees.id eq id)
                .forUpdate()
                .iterator().next()
            val employee = rowToEmployee(row)
            val updatedEmployee = update(employee)
            require(updatedEmployee.id == id) {
                "Id cannot be changed"
            }

            database.update(Employees) {
                set(it.name, updatedEmployee.name)
                set(it.position, updatedEmployee.position)
                set(it.email, updatedEmployee.email)
                set(it.salary, updatedEmployee.salary.minorAmount)
                set(it.modified_at, updatedEmployee.modifiedAt)
                where { it.id eq id }
            }
        }
    }

    fun get(id: UUID): Employee {
        val row = database.from(Employees)
            .select()
            .where(Employees.id eq id)
            .iterator().next()
        return rowToEmployee(row)
    }

    private fun rowToEmployee(row: QueryRowSet) = Employee(
        id = row[Employees.id]!!,
        name = row[Employees.name]!!,
        position = row[Employees.position]!!,
        email = row[Employees.email]!!,
        salary = Money(row[Employees.salary]!!),
        createdAt = row[Employees.created_at]!!,
        modifiedAt = row[Employees.modified_at]!!,
    )
}
