package com.santepriceindex.app.ui.auth

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Login
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.santepriceindex.app.data.Market
import com.santepriceindex.app.ui.components.SectionTitle
import com.santepriceindex.app.ui.theme.SanteColors

@Composable
fun AuthScreen(
    errorMessage: String?,
    onGoogleSignIn: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Brush.verticalGradient(listOf(SanteColors.Slate50, SanteColors.Slate100)))
            .padding(24.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Box(
                modifier = Modifier
                    .background(SanteColors.Indigo, RoundedCornerShape(36.dp))
                    .padding(horizontal = 28.dp, vertical = 22.dp)
            ) {
                Text("SP", color = Color.White, fontSize = 46.sp, fontWeight = FontWeight.ExtraBold)
            }
            Spacer(Modifier.height(22.dp))
            Text("Sante-Price Index", style = MaterialTheme.typography.headlineMedium)
            Text("Fair Prices, Fair Profits", color = SanteColors.Slate500)
            Spacer(Modifier.height(44.dp))
            Button(
                onClick = onGoogleSignIn,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(58.dp),
                shape = RoundedCornerShape(28.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SanteColors.Indigo)
            ) {
                Icon(Icons.Default.Login, contentDescription = null, tint = Color.White)
                Text(
                    "Continue with Google",
                    modifier = Modifier.padding(start = 10.dp),
                    fontWeight = FontWeight.SemiBold
                )
            }
            if (errorMessage != null) {
                Spacer(Modifier.height(18.dp))
                Text(
                    errorMessage,
                    color = SanteColors.Danger,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

@Composable
fun MarketSelectionScreen(
    markets: List<Market>,
    selectedMarket: Market?,
    onMarketSelected: (Market) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SanteColors.Slate50)
            .padding(20.dp)
    ) {
        SectionTitle("Choose Market", "Select the mandi you buy from most often")
        Spacer(Modifier.height(20.dp))
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            contentPadding = PaddingValues(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp),
            horizontalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(markets) { market ->
                val selected = selectedMarket?.city == market.city
                Card(
                    onClick = { onMarketSelected(market) },
                    shape = RoundedCornerShape(28.dp),
                    border = BorderStroke(if (selected) 3.dp else 1.dp, if (selected) SanteColors.Indigo else SanteColors.Slate200),
                    colors = CardDefaults.cardColors(containerColor = Color.White),
                    elevation = CardDefaults.cardElevation(if (selected) 8.dp else 3.dp)
                ) {
                    Column(Modifier.padding(18.dp)) {
                        Icon(Icons.Default.LocationOn, contentDescription = null, tint = SanteColors.Indigo)
                        Spacer(Modifier.height(18.dp))
                        Text(market.city.uppercase(), style = MaterialTheme.typography.titleMedium)
                        Text(market.mandiName, color = SanteColors.Slate500, style = MaterialTheme.typography.bodyMedium)
                        Spacer(Modifier.height(10.dp))
                        Text("Active today", color = SanteColors.Success, style = MaterialTheme.typography.bodyMedium)
                    }
                }
            }
        }
    }
}
