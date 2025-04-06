package com.example.infrastructure.in_memory.book

import com.example.stock_management.domain.models.book.Book
import com.example.stock_management.domain.models.book.IBookRepository
import com.example.stock_management.domain.models.book.book_id.BookId

class InMemoryBookRepository : IBookRepository {
    private val books = mutableMapOf<BookId, Book>()

    override fun save(book: Book): Book {
        books[book.bookId] = book
        return book
    }

    override fun update(book: Book): Book {
        books[book.bookId] = book
        return book
    }

    override fun delete(book: Book): Book? {
        return books.remove(book.bookId)
    }

    override fun find(book: Book): Book? {
        return books[book.bookId]
    }
}