package com.example.auth

import com.auth0.jwt.*
import com.auth0.jwt.algorithms.*
import io.ktor.serialization.kotlinx.json.*
import io.ktor.server.application.*
import io.ktor.server.netty.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import org.example.auth.JWTClaim
import org.example.auth.Role
import java.util.*

@Serializable
data class User(val username: String, val password: String)

fun main(args: Array<String>) = EngineMain.main(args)

fun Application.main() {
    install(ContentNegotiation) {
        json()
    }
    val secret = environment.config.property("jwt.secret").getString()
    val issuer = environment.config.property("jwt.issuer").getString()

    routing {
        post("/login") {
            val user = call.receive<User>()
            // note: it's a stub implementation
            val role = when (user.username) {
                "admin" -> Role.EMPLOYEE_ADMIN
                "manager" -> Role.MANAGER
                else -> Role.EMPLOYEE
            }
            val token = JWT.create()
                .withIssuer(issuer)
                .withClaim(JWTClaim.USERNAME, user.username)
                .withRoles(role)
                .withExpiresAt(Date(System.currentTimeMillis() + 600000))
                .sign(Algorithm.HMAC256(secret))
            call.respond(hashMapOf("token" to token))
        }
    }
}

private fun JWTCreator.Builder.withRoles(vararg roles: Role): JWTCreator.Builder {
    return withArrayClaim(JWTClaim.ROLES, roles.map(Role::name).toTypedArray())
}
