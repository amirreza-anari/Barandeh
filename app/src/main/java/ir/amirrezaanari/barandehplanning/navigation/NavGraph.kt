package ir.amirrezaanari.barandehplanning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.amirrezaanari.barandehplanning.ai.ui.AiScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import ir.amirrezaanari.barandehplanning.homescreen.HomeScreen
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.MainPlannerScreen
import ir.amirrezaanari.barandehplanning.tools.ToolsScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: PlannerViewModel
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
        enterTransition = {
            fadeIn(
                animationSpec = tween(500),
            )
        },
        exitTransition = {
            fadeOut(
                animationSpec = tween(500)
            )
        }
    ) {

        composable(
            BottomNavItem.Home.route,
        ) {
            HomeScreen(viewModel)
        }

        composable(
            BottomNavItem.Planning.route,
        ) {
            MainPlannerScreen(viewModel)
        }

        composable(
            BottomNavItem.Tools.route
        ) {
            ToolsScreen()
        }

        composable(
            BottomNavItem.AiAssistant.route
        ) {
            AiScreen(viewModel)
        }
    }
}
