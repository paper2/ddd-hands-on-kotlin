package com.example.stock_management.domain.models.book.title
import org.junit.jupiter.api.assertThrows
import kotlin.test.*

class TitleTest {
    @Test
    fun `initialize title with valid length`() {
        val title = Title("A valid title")
        assertEquals("A valid title", title.value)
    }

    @Test
    fun `throw exception when title is empty`() {
        assertThrows<IllegalArgumentException> { Title("") }
    }

    @Test
    fun `throw exception when title exceeds maximum length`() {
        val longTitle = "a".repeat(1_001)
        assertThrows<IllegalArgumentException> { Title(longTitle) }
    }

    @Test
    fun `initialize title with maximum length`() {
        val maxLengthTitle = "a".repeat(1_000)
        val title = Title(maxLengthTitle)
        assertEquals(maxLengthTitle, title.value)
    }
}
