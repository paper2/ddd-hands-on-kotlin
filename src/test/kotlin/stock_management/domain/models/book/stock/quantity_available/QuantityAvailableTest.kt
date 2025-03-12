package com.example.stock_management.domain.models.book.stock.quantity_available

import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class QuantityAvailableTest {
    @Test
    fun `initialize QuantityAvailable with valid quantity`() {
        val quantity = QuantityAvailable(500)
        assertEquals(500, quantity.value)
    }

    @Test
    fun `throw exception when quantity is below minimum`() {
        assertThrows<IllegalArgumentException> { QuantityAvailable(0) }
    }

    @Test
    fun `throw exception when quantity exceeds maximum`() {
        assertThrows<IllegalArgumentException> { QuantityAvailable(1_001) }
    }

    @Test
    fun `initialize QuantityAvailable with minimum quantity`() {
        val quantity = QuantityAvailable(1)
        assertEquals(1, quantity.value)
    }

    @Test
    fun `initialize QuantityAvailable with maximum quantity`() {
        val quantity = QuantityAvailable(1_000)
        assertEquals(1_000, quantity.value)
    }
}
