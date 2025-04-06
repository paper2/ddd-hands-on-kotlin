package com.example.stock_management.domain.models.book

import com.example.stock_management.domain.models.book.book_id.BookId
import com.example.stock_management.domain.models.book.price.Price
import com.example.stock_management.domain.models.book.stock.Stock
import com.example.stock_management.domain.models.book.stock.status.Status
import com.example.stock_management.domain.models.book.stock.status.StatusType
import com.example.stock_management.domain.models.book.title.Title

@ConsistentCopyVisibility
data class Book private constructor(
    val bookId: BookId,
    val title: Title,
    val price: Price,
    val stock: Stock,
) {
    companion object {
        fun create(bookId: BookId, title: Title, price: Price) = Book(bookId, title, price, Stock.create())
        fun reconstruct(bookId: BookId, title: Title, price: Price, stock: Stock) = Book(bookId, title, price, stock)
    }

    override fun equals(other: Any?): Boolean {
        return other is Book && bookId == other.bookId
    }

    override fun hashCode(): Int {
        return javaClass.hashCode()
    }

    fun delete(): Book {
        stock.delete()
        return this
    }

    fun changeTitle(title: Title) = Book(bookId, title, price, stock)
    fun changePrice(price: Price) = Book(bookId, title, price, stock)
    fun isSaleable(): Boolean = stock.status != Status(StatusType.OUT_OF_STOCK)
    fun increaseStock(amount: Int) = Book(bookId, title, price, stock.increaseQuantity(amount))
    fun decreaseStock(amount: Int) = Book(bookId, title, price, stock.decreaseQuantity(amount))
}