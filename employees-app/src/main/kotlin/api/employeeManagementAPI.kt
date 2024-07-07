package com.example.api

import com.example.authenticate
import com.example.model.Employee
import com.example.model.Money
import com.example.validateRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.auth.JWTClaim
import org.example.auth.Role
import java.time.Clock
import java.time.Instant
import java.util.UUID

fun Application.setupRoutes(employeeRepository: EmployeeRepository, clock: Clock) {
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
                        createdAt = now,
                        modifiedAt = now,
                    )
                )
                call.respond(created)
            }
        }
    }
}
