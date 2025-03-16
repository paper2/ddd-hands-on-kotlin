package com.example.stock_management.domain.models.book.stock.status

enum class StatusType {
    IN_STOCK,
    LOW_STOCK,
    OUT_OF_STOCK
}

data class Status(val value: StatusType) {
    fun toLabel(): String {
        return when(value) {
            StatusType.IN_STOCK -> "在庫あり"
            StatusType.LOW_STOCK -> "在庫わずか"
            StatusType.OUT_OF_STOCK -> "在庫なし"
        }
    }
}