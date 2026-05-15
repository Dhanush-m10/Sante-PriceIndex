package com.santepriceindex.app.data

import kotlin.math.roundToInt

object PricingEngine {
    data class PricingInput(
        val mandiPrice: Double,
        val transportCost: Double,
        val wastagePercent: Double,
        val profitMargin: Double
    )

    data class PricingOutput(
        val landedCost: Double,
        val wastageCost: Double,
        val effectiveCost: Double,
        val recommendedRetailPrice: Double,
        val netProfit: Double
    )

    fun calculate(input: PricingInput): PricingOutput {
        val safeMandi = input.mandiPrice.coerceAtLeast(0.0)
        val safeTransport = input.transportCost.coerceAtLeast(0.0)
        val safeWastage = input.wastagePercent.coerceIn(0.0, 95.0)
        val safeMargin = input.profitMargin.coerceAtLeast(0.0)

        val landedCost = safeMandi + safeTransport
        val effectiveCost = landedCost / (1 - safeWastage / 100)
        val recommendedRetailPrice = effectiveCost * (1 + safeMargin / 100)
        val netProfit = recommendedRetailPrice - effectiveCost

        return PricingOutput(
            landedCost = landedCost.money(),
            wastageCost = (effectiveCost - landedCost).money(),
            effectiveCost = effectiveCost.money(),
            recommendedRetailPrice = recommendedRetailPrice.money(),
            netProfit = netProfit.money()
        )
    }

    private fun Double.money(): Double = (this * 100).roundToInt() / 100.0
}
