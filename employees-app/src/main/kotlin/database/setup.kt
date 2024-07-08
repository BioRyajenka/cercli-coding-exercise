package com.example.database

import com.example.ApplicationComponents
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import io.ktor.server.application.*
import org.flywaydb.core.Flyway
import javax.sql.DataSource


// note: for a simplicity, same creds are used for DDL & DML.
// on a production-grade service separate creds will be needed

fun Application.setupDBMigrations() {
    val url = environment.config.property("datasource.url").getString()
    val user = environment.config.property("datasource.user").getString()
    val password = environment.config.property("datasource.password").getString()

    Flyway.configure().dataSource(url, user, password).load().migrate()
}

context(ApplicationComponents)
fun createDBConnectionPool(): DataSource {
    val url = config.property("datasource.url").getString()
    val user = config.property("datasource.user").getString()
    val password = config.property("datasource.password").getString()

    val config = HikariConfig()
    config.jdbcUrl = url
    config.username = user
    config.password = password
    return HikariDataSource(config)
}
