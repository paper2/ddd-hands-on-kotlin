package com.example.stock_management.domain.services.book.isbn_duplication_check_domain_service

import com.example.stock_management.domain.models.book.Book
import com.example.stock_management.domain.models.book.IBookRepository

class ISBNDuplicationCheckDomainService(private val bookRepository: IBookRepository) {
    fun execute(book: Book): Boolean = bookRepository.find(book) != null
}