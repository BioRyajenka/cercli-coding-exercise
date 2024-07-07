package com.example.model

import com.example.util.InstantSerializer
import com.example.util.UUIDSerializer
import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.EmailValidator
import java.time.Instant
import java.util.*

// I prefer rich models to anemic models, thus all the validation and functionality
// is performed right in the model
@Serializable
data class Employee(
    @Serializable(with = UUIDSerializer::class)
    val id: UUID,
    val name: String,
    val position: String,
    val email: String,
    @Serializable(with = MoneySerializer::class)
    val salary: Money,
    val countryOfEmployment: Country,
    @Serializable(with = InstantSerializer::class)
    override val createdAt: Instant,
    @Serializable(with = InstantSerializer::class)
    override val modifiedAt: Instant,
): BaseEntity {
    init {
        require(name.isNotBlank()) { "Name should not be blank or empty" }
        require(position.isNotBlank()) { "Position should not be blank or empty" }
        require(EmailValidator.getInstance().isValid(email)) {
            "Email should be valid"
        }
    }
}
