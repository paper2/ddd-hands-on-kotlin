package com.example.stock_management.domain.models.book.stock.stock_id

import java.util.UUID

@JvmInline
value class StockId (val value: UUID = UUID.randomUUID())
