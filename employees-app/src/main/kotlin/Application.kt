package com.example

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.http.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.example.auth.JWTClaim

private const val JWT_AUTH = "auth-jwt"

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    val secret = environment.config.property("jwt.secret").getString()
    val expectedIssuer = environment.config.property("jwt.expected-issuer").getString()
    install(Authentication) {
        jwt(JWT_AUTH) {
            verifier(JWT
                .require(Algorithm.HMAC256(secret))
                .withIssuer(expectedIssuer)
                .build())
            validate { credential ->
                if (credential.payload.getClaim("username").asString() != "") {
                    JWTPrincipal(credential.payload)
                } else {
                    null
                }
            }
            challenge { _, _ ->
                call.respond(HttpStatusCode.Unauthorized, "Token is not valid or has expired")
            }
        }
    }

    routing {
        authenticate(JWT_AUTH) {
            get("/hello") {
                val principal = call.principal<JWTPrincipal>()
                val username = principal!!.payload.getClaim(JWTClaim.USERNAME).asString()
                val expiresAt = principal.expiresAt?.time?.minus(System.currentTimeMillis())
                call.respondText("Hello, $username! Token is expired at $expiresAt ms.")
            }
        }
    }
}
