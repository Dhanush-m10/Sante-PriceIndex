package com.santepriceindex.app.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.santepriceindex.app.data.BoardItem
import com.santepriceindex.app.data.CommodityPrice
import com.santepriceindex.app.data.Market
import com.santepriceindex.app.data.MarketRepository
import com.santepriceindex.app.data.PricePoint
import com.santepriceindex.app.data.PricingEngine
import com.santepriceindex.app.data.UserSession

class SanteAppViewModel : ViewModel() {
    var userSession by mutableStateOf<UserSession?>(null)
        private set

    var selectedCommodity by mutableStateOf<CommodityPrice?>(null)
        private set

    private val _boardItems = mutableStateListOf<BoardItem>()
    val boardItems: List<BoardItem> = _boardItems

    val markets: List<Market> = MarketRepository.markets

    val activeMarket: Market
        get() = userSession?.activeMarket ?: markets.first()

    val prices: List<CommodityPrice>
        get() = MarketRepository.pricesFor(activeMarket)

    fun signIn(email: String, displayName: String) {
        userSession = UserSession(email = email, displayName = displayName)
    }

    fun chooseMarket(market: Market) {
        val current = userSession ?: UserSession(email = "vendor@sante.local", displayName = "Vendor")
        userSession = current.copy(activeMarket = market)
    }

    fun changeCommodity(commodity: CommodityPrice) {
        selectedCommodity = commodity
    }

    fun priceHistory(commodity: String): List<PricePoint> =
        MarketRepository.sevenDayHistory(commodity, activeMarket)

    fun forecast(history: List<PricePoint>): String = MarketRepository.forecastFor(history)

    fun sendToBoard(
        commodity: String,
        unit: String,
        mandiPrice: Double,
        retailPrice: Double
    ) {
        _boardItems.removeAll { it.commodity == commodity }
        _boardItems.add(
            BoardItem(
                commodity = commodity,
                unit = unit,
                mandiPrice = mandiPrice,
                retailPrice = retailPrice
            )
        )
    }

    fun calculate(
        mandiPrice: Double,
        transportCost: Double,
        wastagePercent: Double,
        profitMargin: Double
    ): PricingEngine.PricingOutput =
        PricingEngine.calculate(
            PricingEngine.PricingInput(
                mandiPrice = mandiPrice,
                transportCost = transportCost,
                wastagePercent = wastagePercent,
                profitMargin = profitMargin
            )
        )
}
