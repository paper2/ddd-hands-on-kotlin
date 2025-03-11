package com.example.stock_management.domain.models.book.book_id

@JvmInline
value class BookId(val bookId: String) {

    init {
        validate()
    }

    companion object {
        const val MAX_LENGTH = 13
        const val MIN_LENGTH = 10
    }

    private fun validate() {
        if (bookId.length !in MIN_LENGTH..MAX_LENGTH) {
            throw IllegalArgumentException("ISBNの文字数が不正です")
        }

        if (!isValidIsbn10(bookId) && !isValidIsbn13(bookId)) {
            throw IllegalArgumentException("不正なISBNの形式です")
        }
    }

    private fun isValidIsbn10(isbn10: String): Boolean {
        // ISBN-10のチェックディジット計算をここに実装
        return isbn10.length == 10 // 実際のロジックに置き換える
    }

    private fun isValidIsbn13(isbn13: String): Boolean {
        // ISBN-13の簡易的な検証（実際はもっと詳細なチェックが必要）
        return isbn13.startsWith("978") && isbn13.length == 13
    }

    fun toISBN(): String {
        return if (bookId.length == 10) {
            // ISBNが10桁の場合
            val groupIdentifier = bookId.substring(0, 1)
            val publisherCode = bookId.substring(1, 3)
            val bookCode = bookId.substring(3, 9)
            val checksum = bookId.substring(9)

            "ISBN${groupIdentifier}-${publisherCode}-${bookCode}-${checksum}"
        } else {
            // ISBNが13桁の場合
            val isbnPrefix = bookId.substring(0, 3)
            val groupIdentifier = bookId.substring(3, 4)
            val publisherCode = bookId.substring(4, 6)
            val bookCode = bookId.substring(6, 12)
            val checksum = bookId.substring(12)

            "ISBN${isbnPrefix}-${groupIdentifier}-${publisherCode}-${bookCode}-${checksum}"

        }
    }
}
