package com.example.stock_management.domain.models.book.stock.quantity_available

@JvmInline
value class QuantityAvailable(val quantityAvailable: Int) {

    init {
        validate()
    }

    companion object {
        const val MAX = 1_000
        const val MIN = 1
    }

    private fun validate() {
        if (quantityAvailable !in MIN..MAX) {
            throw IllegalArgumentException("在庫数が不正です")
        }
    }
}
