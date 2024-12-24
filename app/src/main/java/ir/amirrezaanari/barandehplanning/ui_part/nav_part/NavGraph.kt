package ir.amirrezaanari.barandehplanning.ui_part.nav_part

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.ui_part.ai_ui.AiScreen
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.TaskPlannerScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.AddTaskScreen
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.EditTaskScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
) {
    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
    ) {

        composable(
            BottomNavItem.Home.route,
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            }
        ) {
            TaskPlannerScreen(taskViewModel, navController)
        }
        composable(
            "add_task_screen/{selectedTab}",
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
        ) { backStackEntry ->
            val selectedTab = backStackEntry.arguments?.getString("selectedTab")?.toIntOrNull() ?: 0
            AddTaskScreen(taskViewModel, navController, selectedTab)
        }
        composable(
            BottomNavItem.AiAssistant.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
        ) {
            AiScreen()
        }
    }
}
