package com.example.stock_management.domain.models.book.title

@JvmInline
value class Title(val value: String) {

    init {
        validate()
    }

    companion object {
        const val MAX_LENGTH = 1_000
        const val MIN_LENGTH = 1
    }

    private fun validate() {
        if (value.length !in MIN_LENGTH..MAX_LENGTH) {
            throw IllegalArgumentException("タイトルの文字数が不正です")
        }
    }
}
