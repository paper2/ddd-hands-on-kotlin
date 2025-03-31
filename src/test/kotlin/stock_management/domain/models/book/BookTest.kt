package com.example.stock_management.domain.models.book

import com.example.stock_management.domain.models.book.book_id.BookId
import com.example.stock_management.domain.models.book.price.Price
import com.example.stock_management.domain.models.book.stock.Stock
import com.example.stock_management.domain.models.book.stock.quantity_available.QuantityAvailable
import com.example.stock_management.domain.models.book.stock.status.Status
import com.example.stock_management.domain.models.book.stock.status.StatusType
import com.example.stock_management.domain.models.book.stock.stock_id.StockId
import com.example.stock_management.domain.models.book.title.Title
import jdk.jfr.Description
import org.assertj.core.api.Assertions.assertThat
import org.assertj.core.api.Assertions.assertThatThrownBy
import org.junit.Test

class BookTest {
    @Test
    @Description("初期在庫数0の書籍を作成する")
    fun `create book with initial stock`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        assertThat(book.stock.quantityAvailable).isEqualTo(QuantityAvailable(0))
        assertThat(book.stock.status).isEqualTo(Status(StatusType.OUT_OF_STOCK))
    }

    @Test
    @Description("既存の在庫を持つ書籍を再構築する")
    fun `reconstruct book with existing stock`() {
        val bookId = BookId("9784167158057")
        val title = Title("Sample Book")
        val price = Price(1000)
        val stock = Stock.reconstruct(StockId(), QuantityAvailable(10), Status(StatusType.IN_STOCK))
        val book = Book.reconstruct(bookId, title, price, stock)
        assertThat(book.bookId).isEqualTo(bookId)
        assertThat(book.title).isEqualTo(title)
        assertThat(book.price).isEqualTo(price)
        assertThat(book.stock).isEqualTo(stock)
    }

    @Test
    @Description("在庫がある場合は削除できない")
    fun `delete book sets stock to zero`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000)).increaseStock(10)
        assertThatThrownBy { book.delete() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("在庫がある場合削除できません。")
    }

    @Test
    @Description("在庫がない場合は削除できる")
    fun `delete book with zero stock`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        val deletedBook = book.delete()
        assertThat(deletedBook.stock.quantityAvailable).isEqualTo(QuantityAvailable(0))
        assertThat(deletedBook.stock.status).isEqualTo(Status(StatusType.OUT_OF_STOCK))
    }


    @Test
    @Description("本のタイトルを変更する")
    fun `change book title`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        val updatedBook = book.changeTitle(Title("New Title"))
        assertThat(updatedBook.title).isEqualTo(Title("New Title"))
    }

    @Test
    @Description("本の価格を変更する")
    fun `change book price`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        val updatedBook = book.changePrice(Price(1500))
        assertThat(updatedBook.price).isEqualTo(Price(1500))
    }

    @Test
    @Description("在庫がある場合は販売可能")
    fun `book is saleable when in stock`() {
        val book = Book.reconstruct(BookId("9784167158057"), Title("Sample Book"), Price(1000), Stock.create())
            .increaseStock(1)
        assertThat(book.isSaleable()).isTrue()
    }

    @Test
    @Description("在庫がない場合は販売不可")
    fun `book is not saleable when out of stock`() {
        val book = Book.reconstruct(BookId("9784167158057"), Title("Sample Book"), Price(1000), Stock.create())
        assertThat(book.isSaleable()).isFalse()
    }

    @Test
    @Description("在庫を増やす")
    fun `increase book stock`() {
        val stock = Stock.create()
        val book = Book.reconstruct(BookId("9784167158057"), Title("Sample Book"), Price(1000), stock)
        val updatedBook = book.increaseStock(3)
        assertThat(updatedBook.stock.quantityAvailable).isEqualTo(QuantityAvailable(3))
    }

    @Test
    @Description("在庫を減らす")
    fun `decrease book stock`() {
        val stock = Stock.create()
        val book = Book.reconstruct(BookId("9784167158057"), Title("Sample Book"), Price(1000), stock)
        val updatedBook = book.increaseStock(5).decreaseStock(3)
        assertThat(updatedBook.stock.quantityAvailable).isEqualTo(QuantityAvailable(2))
    }

    @Test
    @Description("同じBookIdを持つ本は等しいと判定される")
    fun `books with same BookId are considered equal`() {
        val bookId = BookId("9784167158057")
        val book1 = Book.create(bookId, Title("Sample Book"), Price(1000))
        val book2 = Book.create(bookId, Title("Another Sample Book"), Price(2000))
        assertThat(book1).isEqualTo(book2)
    }

    @Test
    @Description("異なるBookIdを持つ本は等しくないと判定される")
    fun `books with different BookIds are not considered equal`() {
        val book1 = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        val book2 = Book.create(BookId("9784167158058"), Title("Sample Book"), Price(1000))
        assertThat(book1).isNotEqualTo(book2)
    }

    @Test
    @Description("本はnullと等しくないと判定される")
    fun `book is not equal to null`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        assertThat(book).isNotEqualTo(null)
    }

    @Test
    @Description("本は異なる型と等しくないと判定される")
    fun `book is not equal to different type`() {
        val book = Book.create(BookId("9784167158057"), Title("Sample Book"), Price(1000))
        val notABook = "Not a Book"
        assertThat(book).isNotEqualTo(notABook)
    }
}