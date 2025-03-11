package com.example.stock_management.domain.models.book.price

import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class PriceTest {
    @Test
    fun `initialize price with valid amount and currency`() {
        val price = Price(1000, "JPY")
        assertEquals(1000, price.amount)
        assertEquals("JPY", price.currency)
    }

    @Test
    fun `throw exception when currency is not JPY`() {
        assertThrows<IllegalArgumentException> { Price(1000, "USD") }
    }

    @Test
    fun `throw exception when amount is below minimum`() {
        assertThrows<IllegalArgumentException> { Price(0, "JPY") }
    }

    @Test
    fun `throw exception when amount is above maximum`() {
        assertThrows<IllegalArgumentException> { Price(1_000_001, "JPY") }
    }
}