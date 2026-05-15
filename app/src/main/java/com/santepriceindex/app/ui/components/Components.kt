package com.santepriceindex.app.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.santepriceindex.app.data.CommodityPrice
import com.santepriceindex.app.data.PriceTrend
import com.santepriceindex.app.ui.theme.SanteColors

@Composable
fun SectionTitle(title: String, subtitle: String? = null) {
    Column {
        Text(title, style = MaterialTheme.typography.headlineMedium)
        if (subtitle != null) {
            Text(subtitle, style = MaterialTheme.typography.bodyMedium, color = SanteColors.Slate500)
        }
    }
}

@Composable
fun PrimaryAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    Button(
        onClick = onClick,
        modifier = modifier.height(56.dp),
        shape = RoundedCornerShape(24.dp),
        colors = ButtonDefaults.buttonColors(containerColor = SanteColors.Indigo)
    ) {
        Text(text, fontWeight = FontWeight.SemiBold)
    }
}

@Composable
fun SecondaryAction(text: String, onClick: () -> Unit, modifier: Modifier = Modifier) {
    OutlinedButton(
        onClick = onClick,
        modifier = modifier.height(52.dp),
        shape = RoundedCornerShape(20.dp),
        border = BorderStroke(1.dp, SanteColors.Slate200)
    ) {
        Text(text)
    }
}

@Composable
fun CommodityCard(commodity: CommodityPrice, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.dp, SanteColors.Slate200),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(20.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(commodity.name, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.height(4.dp))
                Text(commodity.source, style = MaterialTheme.typography.bodyMedium, color = SanteColors.Slate500)
                Spacer(Modifier.height(10.dp))
                Text(
                    "Rs ${"%.2f".format(commodity.price)}/${commodity.unit}",
                    style = MaterialTheme.typography.headlineMedium,
                    color = SanteColors.Indigo
                )
            }
            TrendBadge(commodity.trend)
        }
    }
}

@Composable
fun TrendBadge(trend: PriceTrend) {
    val color = when (trend) {
        PriceTrend.Rising -> SanteColors.Success
        PriceTrend.Falling -> SanteColors.Danger
        PriceTrend.Stable -> SanteColors.Slate500
    }
    val icon = when (trend) {
        PriceTrend.Rising -> Icons.Default.TrendingUp
        PriceTrend.Falling -> Icons.Default.TrendingDown
        PriceTrend.Stable -> Icons.Default.Remove
    }
    Surface(shape = RoundedCornerShape(50), color = color.copy(alpha = 0.12f)) {
        Icon(
            icon,
            contentDescription = trend.name,
            tint = color,
            modifier = Modifier
                .padding(12.dp)
                .size(24.dp)
        )
    }
}

@Composable
fun SmartTip(text: String) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
        color = SanteColors.Indigo
    ) {
        Row(Modifier.padding(18.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(Icons.Default.WbSunny, contentDescription = null, tint = Color.White)
            Column(Modifier.padding(start = 12.dp)) {
                Text("Smart Tip", color = Color.White, fontWeight = FontWeight.Bold)
                Text(text, color = Color.White.copy(alpha = 0.9f))
            }
        }
    }
}

@Composable
fun MoneyRow(label: String, value: Double, strong: Boolean = false) {
    Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
        Text(label, color = SanteColors.Slate200)
        Text(
            "Rs ${"%.2f".format(value)}",
            color = if (strong) SanteColors.BoardYellow else Color.White,
            fontWeight = if (strong) FontWeight.Bold else FontWeight.Normal
        )
    }
}
