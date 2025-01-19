package ir.amirrezaanari.barandehplanning.ui_part.nav_part

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.ui_part.ai_ui.AiScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirrezaanari.barandehplanning.task_part.PlannerDatabase
import ir.amirrezaanari.barandehplanning.task_part.PlannerRepository
import ir.amirrezaanari.barandehplanning.task_part.PlannerViewModel
import ir.amirrezaanari.barandehplanning.task_part.PlannerViewModelFactory
import ir.amirrezaanari.barandehplanning.task_part.MainPlannerScreen
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.AddTaskScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    taskViewModel: TaskViewModel,
) {

    val context = LocalContext.current
    val database = PlannerDatabase.getInstance(context)
    val repository = PlannerRepository(database.dateDao(), database.taskDao())
    val plannerViewModel: PlannerViewModel = viewModel(factory = PlannerViewModelFactory(repository))

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
//            TaskPlannerScreen(taskViewModel, navController)
            MainPlannerScreen(plannerViewModel)
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
            AiScreen(plannerViewModel)
        }
    }
}
