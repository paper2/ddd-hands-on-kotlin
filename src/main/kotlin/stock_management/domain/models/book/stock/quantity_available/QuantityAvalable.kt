package com.example.stock_management.domain.models.book.stock.quantity_available

@JvmInline
value class QuantityAvailable(val value: Int) {

    init {
        validate()
    }

    companion object {
        const val MAX = 1_000
        const val MIN = 0
    }

    private fun validate() {
        if (value !in MIN..MAX) {
            throw IllegalArgumentException("在庫数が不正です")
        }
    }

    fun increment(amount: Int): QuantityAvailable {
        return QuantityAvailable(value + amount)
    }

    fun decrement(amount: Int): QuantityAvailable {
        return QuantityAvailable(value - amount)
    }
}
