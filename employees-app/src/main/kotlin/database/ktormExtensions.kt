package com.example.database

import org.ktorm.schema.BaseTable
import org.ktorm.schema.Column
import org.ktorm.schema.InstantSqlType
import org.ktorm.schema.SqlType
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
