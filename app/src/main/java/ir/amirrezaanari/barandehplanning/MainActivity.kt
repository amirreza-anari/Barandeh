package ir.amirrezaanari.barandehplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.planning.database.PlannerDatabase
import ir.amirrezaanari.barandehplanning.planning.database.PlannerRepository
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModelFactory
import ir.amirrezaanari.barandehplanning.ui.theme.BarandehPlanningTheme


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = PlannerDatabase.getInstance(this)
        val dateDao = database.dateDao()
        val taskDao = database.taskDao()

        val repository = PlannerRepository(dateDao, taskDao)
        val factory = PlannerViewModelFactory(repository)

        setContent {
            val plannerViewModel: PlannerViewModel = viewModel(factory = factory)
            val mainNavController = rememberNavController()
            BarandehPlanningTheme {
                NavHost(
                    navController = mainNavController,
                    startDestination = "splash_screen",
//                    enterTransition = {
//                        fadeIn(
//                            animationSpec = tween(1000)
//                        )
//                    },
//                    exitTransition = {
//                        fadeOut(
//                            animationSpec = tween(1000)
//                        )
//                    }
                ) {
                    composable("splash_screen") {
                        SplashScreen(mainNavController)
                            }
                    composable("main_screen") {
                        MainScreen(plannerViewModel)
                    }
                }
            }
        }
    }
}