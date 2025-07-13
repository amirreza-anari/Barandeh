package ir.amirrezaanari.barandehplanning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.amirrezaanari.barandehplanning.ai.ui.AiScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.navigation.NavType
import androidx.navigation.navArgument
import ir.amirrezaanari.barandehplanning.homescreen.HomeScreen
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.MainPlannerScreen
import ir.amirrezaanari.barandehplanning.stats.StatsScreen
import ir.amirrezaanari.barandehplanning.tools.PomodoroScreen
import ir.amirrezaanari.barandehplanning.tools.ToolsScreen

const val POMODORO_ROUTE = "pomodoro"


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

        composable(BottomNavItem.Planning.route) {
            // Pass the navController to the MainPlannerScreen
            MainPlannerScreen(viewModel = viewModel, navController = navController)
        }

        composable(
            BottomNavItem.Tools.route
        ) {
            ToolsScreen()
        }

        composable(
            BottomNavItem.Stats.route
        ) {
            StatsScreen()
        }

        composable(
            BottomNavItem.AiAssistant.route
        ) {
            AiScreen(viewModel)
        }

        composable(
            route = "$POMODORO_ROUTE/{title}/{details}",
            arguments = listOf(
                navArgument("title") { type = NavType.StringType },
                navArgument("details") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val title = backStackEntry.arguments?.getString("title") ?: ""
            val details = backStackEntry.arguments?.getString("details") ?: ""
            PomodoroScreen(
                title = title,
                details = details,
                isFromTask = true,
                onNavigateBack = {
                    navController.popBackStack()
                }
                )
        }
    }
}
