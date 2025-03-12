package com.example.stock_management.domain.models.book.book_id

import org.junit.jupiter.api.assertThrows
import kotlin.test.Test
import kotlin.test.assertEquals

class BookIdTest {
    @Test
    fun `BookId can be initialized`() {
        val bookId = BookId("9784167158057")
        assertEquals("9784167158057", bookId.value)
    }

    @Test
    fun `toISBN() 13 digit`() {
        val bookId = BookId("9784167158057")
        assertEquals("ISBN978-4-16-715805-7", bookId.toISBN())
    }

    @Test
    fun `toISBN() 10 digit`() {
        val bookId = BookId("4167158051")
        assertEquals("ISBN4-16-715805-1", bookId.toISBN())
    }

    @Test
    fun `throw exception when invalid digit`() {
        val longIsbn = "1".repeat(101)
        val shortIsbn = "1".repeat(9)
        assertThrows<IllegalArgumentException>{ BookId(longIsbn) }
        assertThrows<IllegalArgumentException> { BookId(shortIsbn) }
    }

    @Test
    fun `throw exception when invalid format`() {
        assertThrows<IllegalArgumentException> { BookId("9994167158057") }
    }
}
