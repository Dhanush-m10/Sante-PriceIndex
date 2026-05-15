package com.santepriceindex.app.data

data class UserSession(
    val email: String,
    val displayName: String,
    val activeMarket: Market? = null
)

data class Market(
    val city: String,
    val mandiName: String
) {
    val id: String = city.lowercase()
}

enum class PriceTrend {
    Rising,
    Falling,
    Stable
}

data class CommodityPrice(
    val name: String,
    val unit: String = "kg",
    val market: String,
    val source: String,
    val price: Double,
    val yesterdayPrice: Double,
    val trend: PriceTrend
)

data class BoardItem(
    val commodity: String,
    val unit: String,
    val mandiPrice: Double,
    val retailPrice: Double
)

data class PricePoint(
    val day: String,
    val price: Double
)
