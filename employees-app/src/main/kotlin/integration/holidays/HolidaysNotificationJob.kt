package com.example.integration.holidays

import com.example.database.EmployeeRepository
import com.example.integration.email.EmailService
import org.slf4j.LoggerFactory

class HolidaysNotificationJob(
    private val employeeRepository: EmployeeRepository,
    private val holidaysService: HolidaysService,
    private val emailService: EmailService,
) {
    companion object {
        private val logger = LoggerFactory.getLogger(HolidaysNotificationJob::class.java)
    }

    fun run() {
        employeeRepository.getAll().forEach { employee ->
            try {
                val holidays = holidaysService.getUpcomingHolidays(employee.countryOfEmployment)
                if (holidays.isNotEmpty()) {
                    emailService.sendEmail(
                        topic = "Upcoming holidays",
                        message = "Hello ${employee.name}!\nHere is the list of upcoming holidays for the next week: ${holidays.joinToString("\n")}",
                        recipient = employee.email,
                    )
                }
            } catch (e: CountryNotSupportedException) {
                logger.error("Could not load holidays for country ${employee.countryOfEmployment}", e)
            }
        }
    }
}
