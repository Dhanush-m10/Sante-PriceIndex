package com.santepriceindex.app.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.santepriceindex.app.ui.auth.AuthScreen
import com.santepriceindex.app.ui.auth.MarketSelectionScreen
import com.santepriceindex.app.ui.dashboard.CalculatorScreen
import com.santepriceindex.app.ui.dashboard.PriceBoardScreen
import com.santepriceindex.app.ui.dashboard.PriceWatchScreen
import com.santepriceindex.app.ui.dashboard.SanteScaffold
import com.santepriceindex.app.ui.dashboard.TrendsScreen
import com.santepriceindex.app.ui.viewmodel.SanteAppViewModel

object Routes {
    const val Auth = "auth"
    const val Market = "market"
    const val Watch = "watch"
    const val Calculator = "calculator"
    const val Trends = "trends"
    const val Board = "board"
}

@Composable
fun SanteNavGraph(
    viewModel: SanteAppViewModel,
    signInError: String?,
    onGoogleSignIn: () -> Unit
) {
    val navController = rememberNavController()
    val backStack by navController.currentBackStackEntryAsState()
    val route = backStack?.destination?.route

    NavHost(navController = navController, startDestination = Routes.Auth) {
        composable(Routes.Auth) {
            AuthScreen(signInError, onGoogleSignIn)
            if (viewModel.userSession != null) {
                LaunchedEffect(Unit) {
                    navController.navigate(Routes.Market) {
                        popUpTo(Routes.Auth) { inclusive = true }
                    }
                }
            }
        }
        composable(Routes.Market) {
            MarketSelectionScreen(
                markets = viewModel.markets,
                selectedMarket = viewModel.userSession?.activeMarket,
                onMarketSelected = {
                    viewModel.chooseMarket(it)
                    navController.navigate(Routes.Watch) {
                        popUpTo(Routes.Market) { inclusive = true }
                    }
                }
            )
        }
        composable(Routes.Watch) {
            SanteScaffold(navController, route) { padding ->
                Box(Modifier.padding(padding)) {
                    PriceWatchScreen(
                        viewModel = viewModel,
                        onCommodityClick = { navController.navigate(Routes.Calculator) },
                        onMarketClick = { navController.navigate(Routes.Market) }
                    )
                }
            }
        }
        composable(Routes.Calculator) {
            SanteScaffold(navController, route) { padding ->
                Box(Modifier.padding(padding)) {
                    CalculatorScreen(viewModel) { navController.navigate(Routes.Board) }
                }
            }
        }
        composable(Routes.Trends) {
            SanteScaffold(navController, route) { padding ->
                Box(Modifier.padding(padding)) {
                    TrendsScreen(viewModel)
                }
            }
        }
        composable(Routes.Board) {
            SanteScaffold(navController, route) { padding ->
                Box(Modifier.padding(padding)) {
                    PriceBoardScreen(viewModel)
                }
            }
        }
    }
}
