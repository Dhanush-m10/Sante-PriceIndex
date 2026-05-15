package com.santepriceindex.app.data

object MarketRepository {
    val markets = listOf(
        Market("Bangalore", "KR Market"),
        Market("Mumbai", "Vashi APMC"),
        Market("Chennai", "Koyambedu"),
        Market("Pune", "Market Yard"),
        Market("Delhi", "Azadpur Mandi")
    )

    private val basePrices = mapOf(
        "Onion" to 22.50,
        "Tomato" to 18.00,
        "Potato" to 26.20,
        "Green Chilli" to 54.00,
        "Garlic" to 138.00
    )

    private val marketMultipliers = mapOf(
        "Bangalore" to 1.04,
        "Mumbai" to 1.10,
        "Chennai" to 0.98,
        "Pune" to 1.00,
        "Delhi" to 0.94
    )

    fun pricesFor(market: Market): List<CommodityPrice> {
        val multiplier = marketMultipliers[market.city] ?: 1.0
        return basePrices.entries.mapIndexed { index, entry ->
            val price = entry.value * multiplier
            val yesterday = price * listOf(0.94, 1.08, 1.0, 0.97, 1.03)[index]
            CommodityPrice(
                name = entry.key,
                market = market.city,
                source = "${market.mandiName} - Fresh today",
                price = price.money(),
                yesterdayPrice = yesterday.money(),
                trend = when {
                    price > yesterday * 1.02 -> PriceTrend.Rising
                    price < yesterday * 0.98 -> PriceTrend.Falling
                    else -> PriceTrend.Stable
                }
            )
        }
    }

    fun sevenDayHistory(commodity: String, market: Market): List<PricePoint> {
        val current = pricesFor(market).first { it.name == commodity }.price
        val movement = when (commodity) {
            "Onion" -> listOf(-3.0, -1.8, -0.4, 0.6, 1.1, 2.2, 0.0)
            "Tomato" -> listOf(2.1, 1.0, -0.6, -1.2, -2.4, -1.8, 0.0)
            "Garlic" -> listOf(-1.2, 0.5, 1.8, 2.2, 2.9, 3.4, 0.0)
            else -> listOf(-0.8, 0.2, 0.6, 0.9, -0.2, 0.4, 0.0)
        }
        val days = listOf("Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun")
        return days.mapIndexed { index, day ->
            PricePoint(day, (current + movement[index]).coerceAtLeast(1.0).money())
        }
    }

    fun forecastFor(history: List<PricePoint>): String {
        if (history.size < 2) return "Use normal buying until more market data is available."
        val change = history.last().price - history.first().price
        return when {
            change > 2.0 -> "Prices are rising. Buy essential stock today and avoid over-committing."
            change < -2.0 -> "Prices are cooling. Wait briefly if your stall has enough stock."
            else -> "Prices look steady. Buy your normal quantity and protect your margin."
        }
    }

    private fun Double.money(): Double = kotlin.math.round(this * 100) / 100.0
}
