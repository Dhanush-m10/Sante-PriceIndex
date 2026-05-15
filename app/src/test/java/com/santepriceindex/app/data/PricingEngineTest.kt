package com.santepriceindex.app.data

import org.junit.Assert.assertEquals
import org.junit.Test

class PricingEngineTest {
    @Test
    fun calculatesRecommendedRetailPrice() {
        val output = PricingEngine.calculate(
            PricingEngine.PricingInput(
                mandiPrice = 20.0,
                transportCost = 3.0,
                wastagePercent = 15.0,
                profitMargin = 25.0
            )
        )

        assertEquals(23.0, output.landedCost, 0.01)
        assertEquals(27.06, output.effectiveCost, 0.01)
        assertEquals(33.82, output.recommendedRetailPrice, 0.01)
        assertEquals(6.76, output.netProfit, 0.01)
    }
}
