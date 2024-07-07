package com.example.api

import com.example.model.Employee
import com.example.model.anEmployee
import com.example.setupAuth
import com.example.setupExceptionHandlers
import com.example.setupObservability
import io.ktor.client.call.*
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.config.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.testing.*
import kotlinx.serialization.json.Json
import org.example.auth.Role
import org.junit.jupiter.api.Test
import org.mockito.kotlin.given
import kotlin.test.assertEquals

class ObservabilityTest {
    @Test
    fun `GET health returns OK`() = testApplication {
        // given
        environment {
            config = MapApplicationConfig() // empty
        }
        application {
            setupObservability()
        }

        // when
        val response = client.get("/health")

        // then
        assertEquals(HttpStatusCode.OK, response.status)
    }
}
