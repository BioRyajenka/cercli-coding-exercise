package com.example.api

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import com.example.model.anEmployee
import com.example.setupAuth
import com.example.setupExceptionHandlers
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.example.auth.JWTClaim
import org.example.auth.Role
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*
import java.time.Clock
import java.time.Instant
import java.time.ZoneId
import java.util.*
import kotlin.test.assertEquals

class EmployeeManagementAPITest {
    private val employeeRepository = mock<EmployeeRepository>()
    private val clock = Clock.fixed(Instant.now(), ZoneId.systemDefault())

    @Test
    fun `POST employees responds with Unauthorized if auth token is not provided`() = testApplication {
        // given
        val (client, _) = setupEnvironmentAndGetClient(Role.EMPLOYEE_ADMIN)

        // when
        val response = client.post("/employees") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            setBody(anEmployee())
        }

        // then
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `POST employees responds with Unauthorized if role is not EMPLOYEE_ADMIN`() = testApplication {
        // given
        val (client, token) = setupEnvironmentAndGetClient(Role.MANAGER, Role.EMPLOYEE)

        // when
        val response = client.post("/employees") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(anEmployee())
        }

        // then
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `POST employees persists employee when input is valid`() = testApplication {
        // given
        val (client, token) = setupEnvironmentAndGetClient(Role.EMPLOYEE_ADMIN)
        given(employeeRepository.save(any())).willReturn(anEmployee())

        // when
        val response = client.post("/employees") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(anEmployee())
        }

        // then
        assertEquals(HttpStatusCode.OK, response.status)
        verify(employeeRepository).save(argThat { employee -> employee.createdAt == clock.instant() })
    }

    @Test
    fun `PATCH employees responds with Unauthorized if role is not EMPLOYEE_ADMIN`() = testApplication {
        // given
        val (client, token) = setupEnvironmentAndGetClient(Role.MANAGER, Role.EMPLOYEE)
        val updateRequest = anEmployee()

        // when
        val response = client.patch("/employees/${updateRequest.id}") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody(updateRequest)
        }

        // then
        assertEquals(HttpStatusCode.Unauthorized, response.status)
    }

    @Test
    fun `PATCH employees updates employee when input is valid`() = testApplication {
        // given
        val (client, token) = setupEnvironmentAndGetClient(Role.EMPLOYEE_ADMIN)
        val id = UUID.randomUUID()

        // when
        val response = client.patch("/employees/$id") {
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            header(HttpHeaders.Authorization, "Bearer $token")
            setBody("""
                {"name": "newname"}
            """.trimIndent())
        }

        // then
        assertEquals(HttpStatusCode.NoContent, response.status)
        verify(employeeRepository).update(eq(id), any())
    }

    private fun ApplicationTestBuilder.setupEnvironmentAndGetClient(vararg roles: Role): Pair<HttpClient, String> {
        val jwtSecret = "secret"
        val jwtIssuer = "issuer"
        val jwtToken = JWT.create()
            .withIssuer(jwtIssuer)
            .withClaim(JWTClaim.USERNAME, "username")
            .withArrayClaim(JWTClaim.ROLES, roles.map(Role::name).toTypedArray())
            .withExpiresAt(Date(System.currentTimeMillis() + 600000))
            .sign(Algorithm.HMAC256(jwtSecret))

        environment {
            config = MapApplicationConfig("jwt.secret" to jwtSecret, "jwt.expected-issuer" to jwtIssuer)
        }

        // all except DB, which is mocked
        application {
            install(ContentNegotiation) {
                // ignore keys is needed because I feed Employee object instead of CreateEmployeeRequest, for simplicity
                json(Json { ignoreUnknownKeys = true })
            }
            setupExceptionHandlers()
            setupAuth()
            setupRoutes(employeeRepository, clock)
        }

        return createClient {
            install(io.ktor.client.plugins.contentnegotiation.ContentNegotiation) {
                json()
            }
        } to jwtToken
    }
}
