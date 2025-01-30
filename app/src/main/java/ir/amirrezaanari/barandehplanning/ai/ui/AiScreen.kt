package ir.amirrezaanari.barandehplanning.ai.ui

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import ir.amirrezaanari.barandehplanning.ai.gemini.ChatRoute
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel

@Composable
fun AiScreen(viewModel: PlannerViewModel){

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val navController = rememberNavController()
        NavHost(
            navController = navController,
            startDestination = "home",
            exitTransition = {
                fadeOut(animationSpec = tween(500))
            },
            enterTransition = {
                fadeIn(animationSpec = tween(500))
            }
        ) {
            composable("home") { AiFirstScreen(navController,viewModel) }
            composable(
                route = "chat/{statistics}", // اضافه کردن آرگومان
                arguments = listOf(navArgument("statistics") { type = NavType.StringType })
            ) { backStackEntry ->
                val statistics = backStackEntry.arguments?.getString("statistics") ?: ""
                ChatRoute(navController, statistics) // پاس دادن statistics به ChatRoute
            }
        }
    }
}