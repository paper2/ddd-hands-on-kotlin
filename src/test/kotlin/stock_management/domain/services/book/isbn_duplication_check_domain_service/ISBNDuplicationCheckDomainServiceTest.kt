package com.example.stock_management.domain.services.book.isbn_duplication_check_domain_service

import com.example.infrastructure.in_memory.book.InMemoryBookRepository
import com.example.infrastructure.jdbi.book.JdbiBookRepository
import com.example.stock_management.domain.models.book.Book
import com.example.stock_management.domain.models.book.IBookRepository
import com.example.stock_management.domain.models.book.book_id.BookId
import com.example.stock_management.domain.models.book.price.Price
import com.example.stock_management.domain.models.book.title.Title
import org.assertj.core.api.Assertions.assertThat
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.junit.Test
import org.junit.Before
import org.junit.jupiter.api.DisplayName

class ISBNDuplicationCheckDomainServiceTest {
    private lateinit var bookRepository: IBookRepository
    private lateinit var isbnDuplicationCheckDomainService: ISBNDuplicationCheckDomainService
    private val existingBook = Book.create(
        BookId("9784167158057"),
        Title("Sample Book"),
        Price(1000)
    )

    @Before
    fun setUp() {
        bookRepository = JdbiBookRepository()
        // TODO: utilに切り出したらここで利用しない
        val jdbi = Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "postgres", "password").installPlugin(KotlinPlugin())
        jdbi.useTransaction<Exception> { handle->
            handle.createUpdate(
                """
                DELETE FROM books;
                DELETE FROM stocks;
            """
            ).execute()
        }
        isbnDuplicationCheckDomainService = ISBNDuplicationCheckDomainService(bookRepository)
        bookRepository.save(existingBook)
    }

    @Test
    @DisplayName("重複がない場合、falseを返す")
    fun testExecute_NoDuplicate() {
        val book = Book.create(
            BookId("9784167158000"),
            Title("Sample Book"),
            Price(1000)
        )
        val result = isbnDuplicationCheckDomainService.execute(book)
        assertThat(result).isFalse
    }

    @Test
    @DisplayName("重複がある場合、trueを返す")
    fun testExecute_WithDuplicate() {
        val result = isbnDuplicationCheckDomainService.execute(existingBook)
        assertThat(result).isTrue
    }
}