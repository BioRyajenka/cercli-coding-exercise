package com.example.database

import com.example.model.Employee
import org.ktorm.schema.Table
import org.ktorm.schema.long
import org.ktorm.schema.text
import org.ktorm.schema.uuid

object Employees : Table<Employee>("t_employee") {
    val id = uuid("id").primaryKey().bindTo { it.id }
    val name = text("name").bindTo { it.name }
    val position = text("position").bindTo { it.position }
    val email = text("email").bindTo { it.email }
    val salary = long("salary_integerised").bindTo { it.salary.minorAmount }
    val created_at = timestampz("created_at").bindTo { it.createdAt }
    val modified_at = timestampz("modified_at").bindTo { it.modifiedAt }
}
