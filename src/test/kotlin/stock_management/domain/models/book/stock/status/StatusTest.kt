package com.example.stock_management.domain.models.book.stock.status

import kotlin.test.*

class StatusTest {
    @Test
    fun `create instance when valid status`() {
        for (type in StatusType.entries) {
            val status = Status(type)
            assertEquals(type, status.value)
        }
    }

    @Test
    fun `convert IN_STOCK status to label`() {
        val status = Status(StatusType.IN_STOCK)
        assertEquals("在庫あり", status.toLabel())
    }

    @Test
    fun `convert LOW_STOCK status to label`() {
        val status = Status(StatusType.LOW_STOCK)
        assertEquals("在庫わずか", status.toLabel())
    }

    @Test
    fun `convert OUT_OF_STOCK status to label`() {
        val status = Status(StatusType.OUT_OF_STOCK)
        assertEquals("在庫なし", status.toLabel())
    }
}