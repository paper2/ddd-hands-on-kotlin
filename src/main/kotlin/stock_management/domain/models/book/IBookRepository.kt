package com.example.stock_management.domain.models.book

interface IBookRepository {
    fun save(book: Book): Book
    fun update(book: Book): Book
    fun delete(book: Book): Book?
    fun find(book: Book): Book?
}