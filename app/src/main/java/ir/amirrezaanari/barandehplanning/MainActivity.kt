package ir.amirrezaanari.barandehplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.database.TaskApp
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.database.TaskViewModelFactory
import ir.amirrezaanari.barandehplanning.ui.theme.BarandehPlanningTheme
import ir.amirrezaanari.barandehplanning.ui_part.MainScreen
import ir.amirrezaanari.barandehplanning.ui_part.SplashScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = (application as TaskApp).database
        val taskDao = database.taskDao()
        val factory = TaskViewModelFactory(taskDao)

        setContent {
            val taskViewModel: TaskViewModel = viewModel(factory = factory)
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
                        MainScreen(taskViewModel)
                    }
                }
//                MainScreen(taskViewModel)
            }
        }
    }
}