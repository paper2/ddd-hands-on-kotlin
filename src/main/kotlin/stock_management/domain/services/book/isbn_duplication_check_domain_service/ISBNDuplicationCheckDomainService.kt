package com.example.stock_management.domain.services.book.isbn_duplication_check_domain_service

import com.example.stock_management.domain.models.book.book_id.BookId

class ISBNDuplicationCheckDomainService {
    fun execute(isbn: BookId): Boolean {
        val isDuplicate = false
        return isDuplicate
    }
}