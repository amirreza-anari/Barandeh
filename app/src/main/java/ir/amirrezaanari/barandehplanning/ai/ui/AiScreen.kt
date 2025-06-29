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
import java.net.URLDecoder

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
                route = "chat/{prompt}?stats={stats}",
                arguments = listOf(
                    navArgument("prompt") {
                        type = NavType.StringType
                    },
                    navArgument("stats") {
                        type = NavType.StringType
                        nullable = true
                        defaultValue = null
                    }
                )
            ) { backStackEntry ->
                val encodedPrompt = backStackEntry.arguments?.getString("prompt") ?: ""
                val prompt = URLDecoder.decode(encodedPrompt, "UTF-8")

                val encodedStats = backStackEntry.arguments?.getString("stats")
                val stats = if (encodedStats != null) URLDecoder.decode(encodedStats, "UTF-8") else ""

                ChatRoute(navController = navController, prompt = prompt, stats = stats)
            }
        }
    }
}