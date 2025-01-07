package ir.amirrezaanari.barandehplanning.ui_part.ai_ui

import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.AiViewModel
import ir.amirrezaanari.barandehplanning.gemini_part.ChatRoute

@Composable
fun AiScreen(){

    val viewModel: AiViewModel = viewModel()

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
            composable("home") { AiFirstScreen(navController, viewModel) }
            composable("chat") { ChatRoute(navController) }
        }
    }
}