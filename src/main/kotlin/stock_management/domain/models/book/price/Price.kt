package com.example.stock_management.domain.models.book.price

data class Price(val amount: Int, val currency: String = "JPY") {

    init {
        validate()
    }

    companion object {
        const val MAX_PRICE = 1_000_000
        const val MIN_PRICE = 1
    }

    private fun validate() {
        if(currency != "JPY") {
            throw IllegalArgumentException("現在は日本円のみ対応しています")
        }
        if(amount !in MIN_PRICE..MAX_PRICE) {
            throw IllegalArgumentException("価格が不正です")
        }
    }
}
