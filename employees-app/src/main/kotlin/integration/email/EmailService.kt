package com.example.integration.email

import com.example.model.Email
import org.slf4j.LoggerFactory

class EmailService {
    companion object {
        private val logger = LoggerFactory.getLogger(EmailService::class.java)
    }

    fun sendEmail(topic: String, message: String, recipient: Email) {
        // note: its a stub. Actual implementation is out of scope for this exercise
        //  ! note that exactly-once guarantee of delivery is a responsibility of this service
        //  this can be achieved by utilising idempotency keys and SMTP service which respects them
        logger.info("Sending an email with topic $topic message $message to $recipient")
    }
}
