package com.example.api

import com.example.setupObservability
import io.ktor.client.request.*
import io.ktor.http.*
import io.ktor.server.config.*
import io.ktor.server.testing.*
import org.junit.jupiter.api.Test
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
