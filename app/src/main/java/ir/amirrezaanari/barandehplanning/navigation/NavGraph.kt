package ir.amirrezaanari.barandehplanning.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import ir.amirrezaanari.barandehplanning.ai.ui.AiScreen
import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import ir.amirrezaanari.barandehplanning.homescreen.HomeScreen
import ir.amirrezaanari.barandehplanning.homescreen.NotesSection
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.MainPlannerScreen


@Composable
fun NavigationGraph(
    navController: NavHostController,
    viewModel: PlannerViewModel
) {

    NavHost(
        navController = navController,
        startDestination = BottomNavItem.Home.route,
    ) {

        composable(
            BottomNavItem.Home.route,
            enterTransition = {
                slideIntoContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Left,
                    animationSpec = tween(500)
                )
            },
            exitTransition = {
                slideOutOfContainer(
                    towards = AnimatedContentTransitionScope.SlideDirection.Right,
                    animationSpec = tween(500)
                )
            }
        ) {
//            NotesSection()
            HomeScreen(navController, viewModel)
        }

        composable(
            BottomNavItem.Planning.route,
            enterTransition = {
                when(initialState.destination.route){
                    BottomNavItem.Home.route ->
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )
                    BottomNavItem.AiAssistant.route ->
                        slideIntoContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )
                    else -> null
                }
            },
            exitTransition = {
                when(targetState.destination.route){
                    BottomNavItem.Home.route ->
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Left,
                            animationSpec = tween(500)
                        )
                    BottomNavItem.AiAssistant.route ->
                        slideOutOfContainer(
                            towards = AnimatedContentTransitionScope.SlideDirection.Right,
                            animationSpec = tween(500)
                        )
                    else -> null
                }
            }
        ) {
            MainPlannerScreen(viewModel)
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
            AiScreen(viewModel)
        }
    }
}
