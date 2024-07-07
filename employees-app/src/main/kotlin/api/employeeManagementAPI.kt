package com.example.api

import com.example.authenticate
import com.example.database.EmployeeRepository
import com.example.model.Employee
import com.example.model.Money
import com.example.validateRole
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.auth.Role
import java.time.Clock
import java.time.Instant
import java.util.UUID

fun Application.setupManagementAPI(employeeRepository: EmployeeRepository, clock: Clock) {
    routing {
        authenticate {
            post("/employees") {
                validateRole(Role.EMPLOYEE_ADMIN)

                val createRequest = call.receive<CreateEmployeeRequest>()
                val now = Instant.now(clock)
                // note: existing solution can be used to map between models
                //  but for simplicity, I did it manually throughout this exercise
                val created = employeeRepository.save(
                    Employee(
                        id = UUID.randomUUID(),
                        name = createRequest.name,
                        position = createRequest.position,
                        email = createRequest.email,
                        salary = Money.fromDouble(createRequest.salary),
                        countryOfEmployment = createRequest.countryOfEmployment,
                        createdAt = now,
                        modifiedAt = now,
                    )
                )
                call.respond(created)
            }
            patch("/employees/{id}") {
                validateRole(Role.EMPLOYEE_ADMIN)

                val id = UUID.fromString(call.parameters["id"]!!)
                val updateRequest = call.receive<UpdateEmployeeRequest>()

                employeeRepository.update(id) { existing ->
                    existing.copy(
                        name = updateRequest.name ?: existing.name,
                        position = updateRequest.position ?: existing.position,
                        email = updateRequest.email ?: existing.email,
                        salary = updateRequest.salary?.let(Money::fromDouble) ?: existing.salary,
                        countryOfEmployment = updateRequest.countryOfEmployment ?: existing.countryOfEmployment,
                        createdAt = existing.createdAt,
                        modifiedAt = Instant.now(clock),
                    )
                }
                call.respond(HttpStatusCode.NoContent)
            }
            get("/employees/{id}") {
                validateRole(Role.MANAGER)

                val id = UUID.fromString(call.parameters["id"]!!)
                call.respond(employeeRepository.get(id))
            }
            get("/employees") {
                validateRole(Role.MANAGER)

                call.respond(employeeRepository.getAll())
            }
        }
    }
}
