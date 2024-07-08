package com.example.model

import kotlinx.serialization.Serializable
import org.apache.commons.validator.routines.EmailValidator

@JvmInline
@Serializable
value class Email(val email: String) {
    init {
        require(EmailValidator.getInstance().isValid(email)) {
            "Email should be valid"
        }
    }
}
