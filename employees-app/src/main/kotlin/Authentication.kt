package com.example

import com.auth0.jwt.JWT
import com.auth0.jwt.algorithms.Algorithm
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.auth.*
import io.ktor.server.auth.jwt.*
import io.ktor.server.response.*
import io.ktor.server.routing.*

private const val JWT_AUTH = "auth-jwt"

fun Route.authenticate(build: Route.() -> Unit): Route {
    return authenticate(JWT_AUTH, build = build)
}

fun Application.setupAuth() {
    val secret = environment.config.property("jwt.secret").getString()
    val expectedIssuer = environment.config.property("jwt.expected-issuer").getString()

    install(Authentication) {
        jwt(JWT_AUTH) {
            verifier(
                JWT
                    .require(Algorithm.HMAC256(secret))
                    .withIssuer(expectedIssuer)
                    .build()
            )
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
}
