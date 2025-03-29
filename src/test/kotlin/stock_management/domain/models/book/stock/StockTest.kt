package com.example.stock_management.domain.models.book.stock

import com.example.stock_management.domain.models.book.stock.quantity_available.QuantityAvailable
import com.example.stock_management.domain.models.book.stock.status.Status
import com.example.stock_management.domain.models.book.stock.status.StatusType
import com.example.stock_management.domain.models.book.stock.stock_id.StockId
import org.assertj.core.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import java.util.*
import kotlin.test.Test

class StockTest {

    private val stockId = StockId(UUID.fromString("00000000-0000-0000-0000-000000000000"))

    @Test
    @DisplayName("デフォルト値で在庫を作成する")
    fun `create stock with default values`() {
        val expectedQuantity = QuantityAvailable(0)
        val expectedStatus = Status(StatusType.OUT_OF_STOCK)

        val stock = Stock.create()

        // UUIDのmockをしてチェックした方が良いけど今回は省略
        assertThat(stock.stockId).isNotEqualTo(StockId())
        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

    @Test
    @DisplayName("在庫を再構築する")
    fun `reconstruct stock`() {
        val expectedQuantity = QuantityAvailable(100)
        val expectedStatus = Status(StatusType.IN_STOCK)

        val stock = Stock.reconstruct(stockId, expectedQuantity, expectedStatus)

        assertThat(stock.stockId).isEqualTo(stockId)
        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

    @Test
    @DisplayName("在庫がある場合は削除できない")
    fun `delete throws error if stock is InStock`() {
        val stock = Stock.create().increaseQuantity(1)

        assertThatThrownBy { stock.delete() }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("在庫がある場合削除できません。")
    }

    @Test
    @DisplayName("在庫なしの場合はエラーを投げない")
    fun `delete does not throw error if stock is OutOfStock`() {
        val stock = Stock.create()

        assertThatCode { stock.delete() }
            .doesNotThrowAnyException()
    }

    @Test
    @DisplayName("在庫数を増やす")
    fun `increase quantity updates available quantity`() {
        val expectedQuantity = QuantityAvailable(105)

        val stock = Stock.create().increaseQuantity(105)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
    }

    @Test
    @DisplayName("増加量が負の数の場合はエラーを投げる")
    fun `increase quantity throws error if amount is negative`() {
        val stock = Stock.create()

        assertThatThrownBy { stock.increaseQuantity(-1) }
            .isInstanceOf(IllegalArgumentException::class.java) // Or the specific exception type thrown
            .hasMessage("増加量は0以上でなければなりません。")
    }


    @Test
    @DisplayName("在庫数を減らせる")
    fun `decrease quantity updates available quantity`() {
        val expectedQuantity = QuantityAvailable(95)

        val stock = Stock.create()
            .increaseQuantity(100)
            .decreaseQuantity(5)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
    }

    @Test
    @DisplayName("減少量が負の数の場合はエラーを投げる")
    fun `decrease quantity throws error if amount is negative`() {
        assertThatThrownBy { Stock.create().decreaseQuantity(-1) }
            .isInstanceOf(IllegalArgumentException::class.java) // Or the specific exception type thrown
            .hasMessage("減少量は0以上でなければなりません。")
    }

    @Test
    @DisplayName("減少後の在庫数が0未満になる場合はエラーを投げる")
    fun `decrease quantity throws error if resulting quantity is negative`() {

        assertThatThrownBy {
            Stock.create()
                .increaseQuantity(10)
                .decreaseQuantity(11)
        }
            .isInstanceOf(IllegalArgumentException::class.java)
            .hasMessage("在庫数が不足しています。")
    }

    @Test
    @DisplayName("減少後の在庫数が0になったらステータスを在庫切れにする")
    fun `decrease quantity to zero changes status to OutOfStock`() {
        val expectedQuantity = QuantityAvailable(0)
        val expectedStatus = Status(StatusType.OUT_OF_STOCK)

        val stock = Stock.create()
            .increaseQuantity(100)
            .decreaseQuantity(100)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

    @Test
    @DisplayName("減少後の在庫数が10以下になったらステータスを残りわずかにする")
    fun `decrease quantity to 10 changes status to LowStock`() {
        val expectedQuantity = QuantityAvailable(1)
        val expectedStatus = Status(StatusType.LOW_STOCK)

        val stock = Stock.create()
            .increaseQuantity(100)
            .decreaseQuantity(99)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

    @Test
    @DisplayName("増加後の在庫数が10より多い場合はステータスを在庫ありにする")
    fun `decrease quantity above 10 changes status to InStock`() {
        val expectedQuantity = QuantityAvailable(95)
        val expectedStatus = Status(StatusType.IN_STOCK)

        val stock = Stock.create().increaseQuantity(95)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

    @Test
    @DisplayName("増加後の在庫数が10以下の場合はステータスを残りわずかにする")
    fun `decrease quantity below 10 changes status to LowStock`() {
        val expectedQuantity = QuantityAvailable(10)
        val expectedStatus = Status(StatusType.LOW_STOCK)

        val stock = Stock.create().increaseQuantity(10)

        assertThat(stock.quantityAvailable).isEqualTo(expectedQuantity)
        assertThat(stock.status).isEqualTo(expectedStatus)
    }

}
