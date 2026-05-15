package com.santepriceindex.app.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SanteScheme = lightColorScheme(
    primary = SanteColors.Indigo,
    onPrimary = Color.White,
    secondary = SanteColors.Slate700,
    background = SanteColors.Slate50,
    surface = Color.White,
    onBackground = SanteColors.Slate900,
    onSurface = SanteColors.Slate900
)

@Composable
fun SantePriceTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = SanteScheme,
        typography = SanteTypography,
        content = content
    )
}
