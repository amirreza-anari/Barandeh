package ir.amirrezaanari.barandehplanning

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.lifecycle.viewmodel.compose.viewModel
import ir.amirrezaanari.barandehplanning.database.TaskApp
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.database.TaskViewModelFactory
import ir.amirrezaanari.barandehplanning.ui.theme.BarandehPlanningTheme
import ir.amirrezaanari.barandehplanning.ui_part.MainScreen


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val database = (application as TaskApp).database
        val taskDao = database.taskDao()
        val factory = TaskViewModelFactory(taskDao)

        setContent {
            val taskViewModel: TaskViewModel = viewModel(factory = factory)
            BarandehPlanningTheme {
                MainScreen(taskViewModel)
            }
        }
    }
}