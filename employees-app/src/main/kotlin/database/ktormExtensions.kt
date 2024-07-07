package com.example.database

import org.ktorm.database.Database
import org.ktorm.dsl.Query
import org.ktorm.expression.QueryExpression
import org.ktorm.expression.SelectExpression
import org.ktorm.expression.SqlFormatter
import org.ktorm.expression.UnionExpression
import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.InstantSqlType
import org.ktorm.schema.SqlType
import org.ktorm.support.postgresql.PostgreSqlFormatter
import java.sql.PreparedStatement
import java.sql.ResultSet
import java.sql.Timestamp
import java.sql.Types
import java.time.Instant

fun BaseTable<*>.timestampz(name: String): Column<Instant> {
    return registerColumn(name, InstantSqlType)
}

object TimestampZSqlType : SqlType<Instant>(Types.TIMESTAMP_WITH_TIMEZONE, "timestampz") {
    override fun doSetParameter(ps: PreparedStatement, index: Int, parameter: Instant) {
        // note: java timestamp is timezone-aware https://stackoverflow.com/questions/14070572/is-java-sql-timestamp-timezone-specific
        ps.setTimestamp(index, Timestamp.from(parameter))
    }

    override fun doGetResult(rs: ResultSet, index: Int): Instant? {
        return rs.getTimestamp(index)?.toInstant()
    }
}

fun Query.forUpdate(): Query {
    val exprForUpdate = when (val expr = this.expression) {
        is SelectExpression -> expr.copy(extraProperties = expr.extraProperties + Pair("forUpdate", true))
        is UnionExpression -> expr.copy(extraProperties = expr.extraProperties + Pair("forUpdate", true))
    }

    return this.withExpression(exprForUpdate)
}

class CustomSqlFormatter(database: Database, beautifySql: Boolean, indentSize: Int)
    : PostgreSqlFormatter(database, beautifySql, indentSize) {

    override fun visitQuery(expr: QueryExpression): QueryExpression {
        super.visitQuery(expr)

        if ("forUpdate" in expr.extraProperties) {
            write("for update ");
        }

        return expr
    }
}
