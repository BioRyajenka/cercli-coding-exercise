package com.example

import io.ktor.server.application.*
import org.flywaydb.core.Flyway

fun Application.setupDBMigrations() {
    val url = environment.config.property("datasource.url").getString()
    val user = environment.config.property("datasource.user").getString()
    val password = environment.config.property("datasource.password").getString()

    // note: this is for simplicity; on a production-grade service separate creds will be needed
    Flyway.configure().dataSource(url, user, password).load().migrate()
}
