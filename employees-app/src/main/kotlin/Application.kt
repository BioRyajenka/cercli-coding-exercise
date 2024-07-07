package com.example

import com.example.api.EmployeeRepository
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
import io.ktor.server.routing.*
import java.time.Clock

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    setupExceptionHandlers()

    setupDBMigrations()
    val dbConnectionPool = createDBConnectionPool()
    val employeeRepository = EmployeeRepository(dbConnectionPool)
    setupAuth()
    setupObservability()
    setupRoutes(employeeRepository, clock = Clock.systemDefaultZone())
}

fun Application.setupObservability() {
    // note: what also can be setup is metrics & traces
    //  for metrics prometheus is a common choice in java;
    //  for traces the common (and a good) choice is opentelemetry + jaeger
    routing {
        get("/health") {
            call.respond("I am healthy")
        }
    }
}

fun Application.setupExceptionHandlers() {
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
        exception<NoSuchElementException> { call, cause ->
            call.respond(HttpStatusCode.NotFound, cause.localizedMessage)
        }
    }
}
