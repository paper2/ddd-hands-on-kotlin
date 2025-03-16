package com.example.stock_management.domain.models.book.stock.status

data class Status(val value: Type) {
    enum class Type {
        IN_STOCK,
        LOW_STOCK,
        OUT_OF_STOCK
    }

    fun toLabel(): String {
        return when(value) {
            Type.IN_STOCK -> "在庫あり"
            Type.LOW_STOCK -> "在庫わずか"
            Type.OUT_OF_STOCK -> "在庫なし"
        }
    }
}