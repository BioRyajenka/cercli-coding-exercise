package com.example

import com.example.api.EmployeeService
import com.example.api.setupRoutes
import com.example.database.createDBConnectionPool
import com.example.database.setupDBMigrations
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    install(StatusPages) {
        exception<IllegalAccessError> { call, cause ->
            call.respond(HttpStatusCode.Unauthorized, cause.localizedMessage)
        }
        exception<IllegalArgumentException> { call, cause ->
            call.respond(HttpStatusCode.BadRequest, cause.localizedMessage)
        }
        exception<NotImplementedError> { call, cause ->
            call.respond(HttpStatusCode.NotImplemented, cause.localizedMessage)
        }
    }

    setupDBMigrations()
    val dbConnectionPool = createDBConnectionPool()
    val employeeService = EmployeeService(dbConnectionPool)
    setupAuth()
    setupRoutes(employeeService)
}
