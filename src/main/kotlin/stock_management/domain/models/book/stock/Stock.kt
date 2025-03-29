package com.example.stock_management.domain.models.book.stock

import com.example.stock_management.domain.models.book.stock.quantity_available.QuantityAvailable
import com.example.stock_management.domain.models.book.stock.status.Status
import com.example.stock_management.domain.models.book.stock.status.StatusType
import com.example.stock_management.domain.models.book.stock.stock_id.StockId

@ConsistentCopyVisibility
data class Stock private constructor(
    val stockId: StockId,
    val quantityAvailable: QuantityAvailable,
    val status: Status
) {
    companion object {
        fun create() = Stock(StockId(), QuantityAvailable(0), Status(StatusType.OUT_OF_STOCK))
        fun reconstruct(
            stockId: StockId,
            quantityAvailable: QuantityAvailable,
            status: Status
        ) = Stock(stockId, quantityAvailable, status)
    }

    fun delete(): Stock {
        if (status.value != StatusType.OUT_OF_STOCK) {
            throw IllegalArgumentException("在庫がある場合削除できません。")
        }
        return Stock(stockId, quantityAvailable, status)
    }

    private fun changeStatusByQuantityAvailable(): Stock =
        when {
            quantityAvailable.value == 0 -> Stock(stockId, quantityAvailable, Status(StatusType.OUT_OF_STOCK))
            quantityAvailable.value in 1..10 -> Stock(stockId, quantityAvailable, Status(StatusType.LOW_STOCK))
            quantityAvailable.value > 10 -> Stock(stockId, quantityAvailable, Status(StatusType.IN_STOCK))
            else -> throw IllegalArgumentException("在庫数が不正です")
        }

    private fun changeQuantityAvailable(quantityAvailable: QuantityAvailable) =
        Stock(stockId, quantityAvailable, status).changeStatusByQuantityAvailable()

    fun increaseQuantity(amount: Int): Stock {
        if (amount < 0) {
            throw IllegalArgumentException("増加量は0以上でなければなりません。")
        }
        return changeQuantityAvailable(quantityAvailable.increment(amount))
    }

    fun decreaseQuantity(amount: Int): Stock {
        if (amount < 0) {
            throw IllegalArgumentException("減少量は0以上でなければなりません。")
        }
        if (quantityAvailable.value - amount < 0) {
            throw IllegalArgumentException("在庫数が不足しています。")
        }
        return changeQuantityAvailable(quantityAvailable.decrement(amount))
    }
}
