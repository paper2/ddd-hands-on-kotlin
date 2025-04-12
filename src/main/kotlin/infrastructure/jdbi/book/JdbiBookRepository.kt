package com.example.infrastructure.jdbi.book

import com.example.stock_management.domain.models.book.Book
import com.example.stock_management.domain.models.book.IBookRepository
import com.example.stock_management.domain.models.book.book_id.BookId
import com.example.stock_management.domain.models.book.price.Price
import com.example.stock_management.domain.models.book.stock.Stock
import com.example.stock_management.domain.models.book.stock.quantity_available.QuantityAvailable
import com.example.stock_management.domain.models.book.stock.status.Status
import com.example.stock_management.domain.models.book.stock.status.StatusType
import com.example.stock_management.domain.models.book.stock.stock_id.StockId
import com.example.stock_management.domain.models.book.title.Title
import org.jdbi.v3.core.Jdbi
import org.jdbi.v3.core.kotlin.KotlinPlugin
import org.jdbi.v3.core.kotlin.mapTo
import org.jdbi.v3.core.transaction.TransactionIsolationLevel
import java.util.*

// TODO: DB周りの操作の抽象化とテスト作成。
// https://zenn.dev/yamachan0625/books/ddd-hands-on/viewer/chapter12_repository ではprisma使っている。今回はjdbiで実装してみた。ただ、util周り整備しないと結構テストが面倒くさい。他の学習もしたいのでここは一旦保留。
class JdbiBookRepository() : IBookRepository {
    private val jdbi =
        Jdbi.create("jdbc:postgresql://localhost:5432/postgres", "postgres", "password").installPlugin(KotlinPlugin());

    override fun save(book: Book): Book {
        // TODO: transaction管理を抽象化する
        jdbi.useTransaction<Exception>(TransactionIsolationLevel.SERIALIZABLE) { handle ->
            // 書籍データの保存
            handle.createUpdate(
                """
                INSERT INTO books (book_id, title, price_amount) 
                VALUES (:bookId, :title, :priceAmount)
            """
            )
                .bind("bookId", book.bookId.value)
                .bind("title", book.title.value)
                .bind("priceAmount", book.price.amount)
                .execute()

            // 在庫データの保存
            handle.createUpdate(
                """
                INSERT INTO stocks (stock_id, book_id, quantity_available, status) 
                VALUES (:stockId, :bookId, :quantityAvailable, :status::status)
            """
            )
                .bind("stockId", book.stock.stockId.value)
                .bind("bookId", book.bookId.value)
                .bind("quantityAvailable", book.stock.quantityAvailable.value)
                .bind("status", book.stock.status.value)
                .execute()
        }
        return book
    }

    override fun update(book: Book): Book {
        jdbi.useTransaction<Exception>(TransactionIsolationLevel.SERIALIZABLE) { handle ->
            // 書籍データの更新
            handle.createUpdate(
                """
                UPDATE books 
                SET title = :title, price_amount = :priceAmount
                WHERE book_id = :bookId
            """
            )
                .bind("title", book.title.value)
                .bind("priceAmount", book.price.amount)
                .bind("bookId", book.bookId.value)
                .execute()

            // 在庫データの更新
            handle.createUpdate(
                """
                UPDATE stocks 
                SET quantity_available = :quantityAvailable, status = :status
                WHERE book_id = :bookId
            """
            )
                .bind("quantityAvailable", book.stock.quantityAvailable.value)
                .bind("status", book.stock.status.value)
                .bind("bookId", book.bookId.value)
                .execute()
        }
        return book
    }

    override fun delete(book: Book): Book {
        jdbi.useTransaction<Exception>(TransactionIsolationLevel.SERIALIZABLE) { handle ->
            // 在庫データの削除（外部キー制約があると仮定）
            handle.createUpdate("DELETE FROM stocks WHERE book_id = :bookId")
                .bind("bookId", book.bookId.value)
                .execute()

            // 書籍データの削除
            handle.createUpdate("DELETE FROM books WHERE book_id = :bookId")
                .bind("bookId", book.bookId.value)
                .execute()
        }
        return book
    }

    override fun find(book: Book): Book? {
        val row = jdbi.withHandle<BookRecord?, Exception> { handle ->
            return@withHandle handle.createQuery(
                """
                SELECT b.book_id, b.title, b.price_amount, 
                       s.stock_id, s.quantity_available, s.status
                FROM books b
                JOIN stocks s ON b.book_id = s.book_id
                WHERE b.book_id = :bookId
            """
            )
                .bind("bookId", book.bookId.value)
                .mapTo<BookRecord>()
                .singleOrNull()
        }
        return row?.let {
            Book.reconstruct(
                BookId(it.bookId),
                Title(it.title),
                Price(it.priceAmount),
                Stock.reconstruct(
                    StockId(it.stockId),
                    QuantityAvailable(it.quantityAvailable),
                    Status(it.status)
                )
            )
        }
    }

    // 書籍と在庫情報を含む1行分のレコードを表すデータクラス
    private data class BookRecord(
        val bookId: String,
        val title: String,
        val priceAmount: Int,
        val stockId: UUID,
        val quantityAvailable: Int,
        val status: StatusType
    )
}