package com.example.api

import com.example.authenticate
import com.example.validateRole
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.auth.JWTClaim
import org.example.auth.Role

fun Application.setupRoutes(employeeService: EmployeeService) {
    routing {
        authenticate {
            post("/employees") {
                validateRole(Role.EMPLOYEE_ADMIN)

                val createRequest = call.receive<CreateEmployeeRequest>()
                val created = employeeService.create(createRequest)
                call.respond(created)
            }
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim(JWTClaim.USERNAME).asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}
