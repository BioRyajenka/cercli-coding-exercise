package com.example.database

import org.junit.jupiter.api.Test

class EmployeeRepositoryTest {
    // TODO (out of scope for this exercise): add integration tests with containerised postgres
    //  using testcontainers library

    @Test
    fun `save persists employee`() {}

    @Test
    fun `save throws DuplicateEntityException when called and employee already exists`() {}

    @Test
    fun `update throws IllegalArgumentException if passed lambda tries to modify id`() {}

    @Test
    fun `update throws NotFoundException if employee doesn't exist`() {}

    @Test
    fun `update updates employee if it exists`() {}

    @Test
    fun `get throws NotFoundException if employee doesn't exist`() {}

    @Test
    fun `get returns employee if it exists`() {}

    @Test
    fun `getAll returns all employees`() {}
}
