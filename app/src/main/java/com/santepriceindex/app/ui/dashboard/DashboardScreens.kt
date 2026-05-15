package com.santepriceindex.app.ui.dashboard

import android.app.Activity
import android.view.WindowManager
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Analytics
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Dashboard
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.santepriceindex.app.data.CommodityPrice
import com.santepriceindex.app.data.Market
import com.santepriceindex.app.data.PricePoint
import com.santepriceindex.app.ui.components.CommodityCard
import com.santepriceindex.app.ui.components.MoneyRow
import com.santepriceindex.app.ui.components.PrimaryAction
import com.santepriceindex.app.ui.components.SectionTitle
import com.santepriceindex.app.ui.components.SmartTip
import com.santepriceindex.app.ui.theme.SanteColors
import com.santepriceindex.app.ui.viewmodel.SanteAppViewModel

sealed class MainTab(val route: String, val label: String, val icon: ImageVector) {
    data object Watch : MainTab("watch", "Watch", Icons.Default.Dashboard)
    data object Calculator : MainTab("calculator", "Calc", Icons.Default.Calculate)
    data object Trends : MainTab("trends", "Trends", Icons.Default.Analytics)
    data object Board : MainTab("board", "Board", Icons.Default.Storefront)
}

@Composable
fun SanteScaffold(
    navController: NavHostController,
    currentRoute: String?,
    content: @Composable (PaddingValues) -> Unit
) {
    val tabs = listOf(MainTab.Watch, MainTab.Calculator, MainTab.Trends, MainTab.Board)
    Scaffold(
        containerColor = SanteColors.Slate50,
        content = content,
        bottomBar = {
            NavigationBar(containerColor = Color.White) {
                tabs.forEach { tab ->
                    NavigationBarItem(
                        selected = currentRoute == tab.route,
                        onClick = { navController.navigate(tab.route) { launchSingleTop = true } },
                        icon = { Icon(tab.icon, contentDescription = tab.label) },
                        label = { Text(tab.label) }
                    )
                }
            }
        }
    )
}

@Composable
fun PriceWatchScreen(
    viewModel: SanteAppViewModel,
    onCommodityClick: (CommodityPrice) -> Unit,
    onMarketClick: () -> Unit
) {
    val prices = viewModel.prices
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.Top) {
                SectionTitle("Price Watch", "Updated just now")
                MarketPill(viewModel.activeMarket, onMarketClick)
            }
        }
        items(prices, key = { it.name }) { price ->
            CommodityCard(price) {
                viewModel.changeCommodity(price)
                onCommodityClick(price)
            }
        }
        item {
            SmartTip("Tomato prices are down today. Good buy if your stall sells fast.")
        }
    }
}

@Composable
private fun MarketPill(market: Market, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(18.dp),
        color = SanteColors.Slate100
    ) {
        Text(
            market.city,
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 9.dp),
            color = SanteColors.Slate700,
            fontWeight = FontWeight.SemiBold
        )
    }
}

@Composable
fun CalculatorScreen(
    viewModel: SanteAppViewModel,
    onBoard: () -> Unit
) {
    val prices = viewModel.prices
    var selected by remember { mutableStateOf(viewModel.selectedCommodity ?: prices.first()) }
    var mandiText by remember(selected) { mutableStateOf("%.2f".format(selected.price)) }
    var transportText by remember { mutableStateOf("3.00") }
    var wastage by remember { mutableFloatStateOf(15f) }
    var margin by remember { mutableFloatStateOf(25f) }

    val mandi = mandiText.toDoubleOrNull() ?: 0.0
    val transport = transportText.toDoubleOrNull() ?: 0.0
    val result = viewModel.calculate(mandi, transport, wastage.toDouble(), margin.toDouble())

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionTitle("Profit Calculator", "Smart pricing for your business") }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(prices, key = { it.name }) { price ->
                    FilterChip(
                        selected = selected.name == price.name,
                        onClick = { selected = price },
                        label = { Text(price.name) }
                    )
                }
            }
        }
        item {
            Card(shape = RoundedCornerShape(24.dp), colors = CardDefaults.cardColors(containerColor = Color.White)) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(14.dp)) {
                    OutlinedTextField(
                        value = mandiText,
                        onValueChange = { mandiText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Mandi Price (Rs/kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = transportText,
                        onValueChange = { transportText = it },
                        modifier = Modifier.fillMaxWidth(),
                        label = { Text("Transport Cost (Rs/kg)") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        singleLine = true
                    )
                    SliderBlock("Wastage Loss", wastage, "0%", "50%", 0f..50f) { wastage = it }
                    SliderBlock("Target Profit", margin, "0%", "100%", 0f..100f) { margin = it }
                }
            }
        }
        item {
            ResultCard(
                commodity = selected.name,
                unit = selected.unit,
                mandiPrice = mandi,
                transportCost = transport,
                wastagePercent = wastage.toDouble(),
                profitPercent = margin.toDouble(),
                result = result,
                onSend = {
                    viewModel.sendToBoard(selected.name, selected.unit, mandi, result.recommendedRetailPrice)
                    onBoard()
                }
            )
        }
        item {
            LiteracyPanel(result.recommendedRetailPrice, result.effectiveCost)
        }
    }
}

@Composable
private fun SliderBlock(
    label: String,
    value: Float,
    startLabel: String,
    endLabel: String,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit
) {
    Column {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(label, fontWeight = FontWeight.SemiBold)
            Text("${value.toInt()}%", color = SanteColors.Indigo, fontWeight = FontWeight.Bold)
        }
        Slider(value = value, onValueChange = onChange, valueRange = range)
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
            Text(startLabel, color = SanteColors.Slate500)
            Text(endLabel, color = SanteColors.Slate500)
        }
    }
}

@Composable
private fun ResultCard(
    commodity: String,
    unit: String,
    mandiPrice: Double,
    transportCost: Double,
    wastagePercent: Double,
    profitPercent: Double,
    result: com.santepriceindex.app.data.PricingEngine.PricingOutput,
    onSend: () -> Unit
) {
    Card(shape = RoundedCornerShape(26.dp), colors = CardDefaults.cardColors(containerColor = SanteColors.Slate900)) {
        Column(Modifier.padding(22.dp), verticalArrangement = Arrangement.spacedBy(12.dp)) {
            Text("RECOMMENDED PRICE", color = SanteColors.Slate200, fontWeight = FontWeight.Bold)
            Text(
                "Rs ${"%.2f".format(result.recommendedRetailPrice)}",
                color = SanteColors.BoardYellow,
                fontSize = 54.sp,
                fontWeight = FontWeight.ExtraBold,
                lineHeight = 58.sp
            )
            Text("per $unit for $commodity", color = SanteColors.Slate200)
            Spacer(Modifier.height(8.dp))
            MoneyRow("Mandi Price", mandiPrice)
            MoneyRow("Transport", transportCost)
            MoneyRow("Landed Cost", result.landedCost)
            MoneyRow("Wastage (${wastagePercent.toInt()}%)", result.wastageCost)
            MoneyRow("Effective Cost", result.effectiveCost)
            MoneyRow("Profit (${profitPercent.toInt()}%)", result.netProfit)
            MoneyRow("Retail Price", result.recommendedRetailPrice, strong = true)
            PrimaryAction("Send to Price Board", onClick = onSend, modifier = Modifier.fillMaxWidth())
        }
    }
}

@Composable
private fun LiteracyPanel(retailPrice: Double, effectiveCost: Double) {
    val revenue = retailPrice * 100
    val cost = effectiveCost * 100
    val profit = revenue - cost
    Surface(shape = RoundedCornerShape(24.dp), color = SanteColors.Slate100) {
        Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
            Text("Understanding Your Profit", style = MaterialTheme.typography.titleMedium)
            Text("For a 100 kg batch, revenue is Rs ${"%.0f".format(revenue)} and estimated total cost is Rs ${"%.0f".format(cost)}.")
            Text("Net profit: Rs ${"%.0f".format(profit)}", color = SanteColors.Success, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
fun TrendsScreen(viewModel: SanteAppViewModel) {
    val prices = viewModel.prices
    var selected by remember { mutableStateOf(viewModel.selectedCommodity ?: prices.first()) }
    val history = viewModel.priceHistory(selected.name)

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(20.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item { SectionTitle("Market Trends", selected.name) }
        item {
            LazyRow(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                items(prices, key = { it.name }) { price ->
                    FilterChip(
                        selected = selected.name == price.name,
                        onClick = { selected = price },
                        label = { Text(price.name) }
                    )
                }
            }
        }
        item { PriceChart(history) }
        item { TrendSummary(history) }
        item {
            Surface(shape = RoundedCornerShape(24.dp), color = Color.White) {
                Column(Modifier.padding(18.dp), verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("AI Forecast", style = MaterialTheme.typography.titleMedium)
                    Text(viewModel.forecast(history), color = SanteColors.Slate700)
                    Text("Confidence: High", color = SanteColors.Indigo, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun PriceChart(history: List<PricePoint>) {
    val min = history.minOf { it.price }
    val max = history.maxOf { it.price }
    Surface(shape = RoundedCornerShape(24.dp), color = Color.White) {
        Column(Modifier.padding(18.dp)) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
            ) {
                val range = (max - min).coerceAtLeast(1.0)
                val stepX = size.width / (history.size - 1).coerceAtLeast(1)
                val points = history.mapIndexed { index, point ->
                    Offset(
                        x = stepX * index,
                        y = size.height - (((point.price - min) / range).toFloat() * size.height)
                    )
                }
                for (i in 0..4) {
                    val y = size.height * i / 4
                    drawLine(SanteColors.Slate200, Offset(0f, y), Offset(size.width, y), strokeWidth = 1.5f)
                }
                val path = Path().apply {
                    points.forEachIndexed { index, point ->
                        if (index == 0) moveTo(point.x, point.y) else lineTo(point.x, point.y)
                    }
                }
                drawPath(path, SanteColors.Indigo, style = Stroke(width = 8f, cap = StrokeCap.Round))
                points.forEach { drawCircle(SanteColors.Indigo, radius = 7f, center = it) }
            }
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                history.forEach { Text(it.day, color = SanteColors.Slate500, fontSize = 12.sp) }
            }
        }
    }
}

@Composable
private fun TrendSummary(history: List<PricePoint>) {
    val change = ((history.last().price - history.first().price) / history.first().price) * 100
    val rising = change > 1
    Surface(shape = RoundedCornerShape(24.dp), color = if (rising) Color(0xFFEAFBF1) else Color(0xFFFFF1F2)) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(if (rising) Icons.Default.Add else Icons.Default.Remove, contentDescription = null, tint = if (rising) SanteColors.Success else SanteColors.Danger)
            Spacer(Modifier.width(12.dp))
            Column {
                Text("7-Day Movement", fontWeight = FontWeight.Bold)
                Text("${if (change >= 0) "+" else ""}${"%.1f".format(change)}%", style = MaterialTheme.typography.headlineMedium)
            }
        }
    }
}

@Composable
fun PriceBoardScreen(viewModel: SanteAppViewModel) {
    val activity = LocalContext.current as? Activity
    DisposableEffect(activity) {
        activity?.window?.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        val oldBrightness = activity?.window?.attributes?.screenBrightness
        activity?.window?.let { window ->
            window.attributes = window.attributes.apply { screenBrightness = 1f }
        }
        onDispose {
            activity?.window?.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
            activity?.window?.let { window ->
                if (oldBrightness != null) {
                    window.attributes = window.attributes.apply { screenBrightness = oldBrightness }
                }
            }
        }
    }

    var fontSize by remember { mutableFloatStateOf(56f) }
    val items = viewModel.boardItems.ifEmpty {
        viewModel.prices.take(3).map {
            com.santepriceindex.app.data.BoardItem(it.name, it.unit, it.price, it.price * 1.3)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Black)
            .padding(18.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text("TODAY'S FAIR PRICES", color = SanteColors.BoardYellow, fontWeight = FontWeight.ExtraBold)
            Row {
                BoardControl(Icons.Default.Remove) { fontSize = (fontSize - 4).coerceAtLeast(34f) }
                Spacer(Modifier.width(8.dp))
                BoardControl(Icons.Default.Add) { fontSize = (fontSize + 4).coerceAtMost(80f) }
            }
        }
        items.forEach { item ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(SanteColors.Slate900, RoundedCornerShape(16.dp))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(Modifier.weight(1f)) {
                    Text(item.commodity.uppercase(), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
                    Text("Mandi Rs ${"%.2f".format(item.mandiPrice)}", color = Color.White.copy(alpha = 0.7f))
                }
                Text(
                    "Rs ${"%.0f".format(item.retailPrice)}",
                    color = SanteColors.BoardYellow,
                    fontSize = fontSize.sp,
                    fontWeight = FontWeight.ExtraBold,
                    textAlign = TextAlign.End,
                    lineHeight = fontSize.sp
                )
            }
        }
    }
}

@Composable
private fun BoardControl(icon: ImageVector, onClick: () -> Unit) {
    Box(
        modifier = Modifier
            .size(42.dp)
            .background(SanteColors.BoardYellow, RoundedCornerShape(50))
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Icon(icon, contentDescription = null, tint = Color.Black)
    }
}
