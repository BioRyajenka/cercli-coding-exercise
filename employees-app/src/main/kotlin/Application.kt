package com.example

import com.example.api.setupManagementAPI
import com.example.database.EmployeeRepository
import com.example.database.createDBConnectionPool
import com.example.database.setupDBMigrations
import com.example.integration.email.EmailService
import com.example.integration.holidays.HolidaysNotificationJob
import com.example.integration.holidays.HolidaysService
import com.example.integration.holidays.OpenHolidaysAPI
import com.example.model.Country
import com.neovisionaries.i18n.CountryCode
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.plugins.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.plugins.statuspages.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.json.Json
import org.slf4j.LoggerFactory
import java.time.Clock
import kotlin.concurrent.fixedRateTimer
import kotlin.time.Duration.Companion.days
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation.Plugin as ClientContentNegotiation
import io.ktor.server.plugins.contentnegotiation.ContentNegotiation as ServerContentNegotiation

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    install(ServerContentNegotiation) {
        json()
    }
    setupExceptionHandlers()
    setupDBMigrations()

    with(ApplicationComponents(environment.config)) {
        setupAuth()
        setupObservability()
        setupJobs()
        setupManagementAPI(employeeRepository, clock)
    }
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

context(ApplicationComponents)
private fun setupJobs() {
    fixedRateTimer(daemon = true, period = 1.days.inWholeMilliseconds) {
        try {
            holidaysNotificationJob.run()
        } catch (e: Exception) {
            setupLogger.error("Error running job", e)
        }
    }
}

private fun createHttpClient(): HttpClient {
    return HttpClient(CIO) {
        install(ClientContentNegotiation) {
            json(Json { ignoreUnknownKeys = true })
        }
        install(HttpRequestRetry) {
            retryOnServerErrors(maxRetries = 5)
            exponentialDelay()
        }
    }
}

// note: I preferred revolut-style manual configuration to DI in this project
class ApplicationComponents(val config: ApplicationConfig) {
    val setupLogger = LoggerFactory.getLogger(ApplicationComponents::class.java)

    val clock = Clock.systemDefaultZone()
    val client = createHttpClient()
    val dbConnectionPool = createDBConnectionPool()
    val employeeRepository = EmployeeRepository(dbConnectionPool)
    val openHolidaysAPI = OpenHolidaysAPI(client, clock)
    val holidaysService = HolidaysService(openHolidaysAPI, clock)
    val emailService = EmailService()
    val holidaysNotificationJob = HolidaysNotificationJob(employeeRepository, holidaysService, emailService)
}
