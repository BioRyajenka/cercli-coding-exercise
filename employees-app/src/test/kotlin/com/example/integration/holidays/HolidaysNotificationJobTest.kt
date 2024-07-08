package com.example.integration.holidays

import com.example.database.EmployeeRepository
import com.example.eqInline
import com.example.integration.email.EmailService
import com.example.model.*
import org.junit.jupiter.api.Test
import org.mockito.kotlin.*

class HolidaysNotificationJobTest {
    private val employeeRepository = mock<EmployeeRepository>()
    private val holidaysService = mock<HolidaysService>()
    private val emailService = mock<EmailService>()
    private val notificationJob = HolidaysNotificationJob(
        employeeRepository, holidaysService, emailService
    )

    @Test
    fun `run should not stop processing when one of the countries is unavailable`() {
        // given
        val employee1 = anEmployee(countryOfEmployment = UAE, email = Email("employee1@mail.com"))
        val employee2 = anEmployee(countryOfEmployment = UK, email = Email("employee2@mail.com"))

        given(employeeRepository.getAll()).willReturn(listOf(employee1, employee2))
        given(holidaysService.getUpcomingHolidays(UAE)).willThrow(CountryNotSupportedException(UAE))
        given(holidaysService.getUpcomingHolidays(UK)).willReturn(listOf(aHoliday(), aHoliday()))

        // when
        notificationJob.run()

        // then
        verify(emailService, never()).sendEmail(any(), any(), eq(employee1.email))
        verify(emailService).sendEmail(any(), any(), eqInline(employee2.email, Email::email, ::argWhere))
    }

    @Test
    fun `run should not send email when there are no upcoming holidays`() {
        // given
        val employee = anEmployee()
        given(employeeRepository.getAll()).willReturn(listOf(employee))
        given(holidaysService.getUpcomingHolidays(employee.countryOfEmployment)).willReturn(emptyList())

        // when
        notificationJob.run()

        // then
        verify(emailService, never()).sendEmail(any(), any(), eq(employee.email))
    }
}
