package com.example.stock_management.domain.models.book.stock.stockId

import com.example.stock_management.domain.models.book.stock.stock_id.StockId
import java.util.UUID
import kotlin.test.*

class StockIdTest {
    @Test
    fun `initialize StockId with valid UUID`() {
        val uuid = UUID.randomUUID()
        val stockId = StockId(uuid)
        assertEquals(uuid, stockId.value)
    }
}