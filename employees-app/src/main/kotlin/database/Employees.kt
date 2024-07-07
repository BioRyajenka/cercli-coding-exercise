package com.example.database

import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.text
import org.ktorm.schema.uuid

object Employees : Table<Nothing>("employees") {
    val id = uuid("id").primaryKey()
    val name = text("name")
    val position = text("position")
    val email = text("email")
    val salary = long("salary_integerised")
    val countryOfEmployment = text("country_of_employment")
    val created_at = timestampz("created_at")
    val modified_at = timestampz("modified_at")
}
