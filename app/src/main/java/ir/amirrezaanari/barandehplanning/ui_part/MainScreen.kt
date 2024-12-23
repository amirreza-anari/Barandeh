package ir.amirrezaanari.barandehplanning.ui_part

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.ui_part.nav_part.BottomNavigationBar
import ir.amirrezaanari.barandehplanning.ui_part.nav_part.NavigationGraph
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.runtime.collectAsState


@Composable
fun MainScreen(taskViewModel: TaskViewModel) {
    val navController = rememberNavController()
    val currentRoute = navController.currentBackStackEntryFlow.collectAsState(initial = null).value?.destination?.route

    Scaffold(
        bottomBar = {
            if (currentRoute != "add_task_screen") {
                BottomNavigationBar(navController)
            }
        }
    ) { paddingValues -> // دریافت فضای خالی برای محتوای اصلی
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    if (currentRoute == "add_task_screen") PaddingValues(0.dp) else paddingValues
                )

        ) {
            NavigationGraph(navController, taskViewModel = taskViewModel)
        }
    }
}

@Composable
fun AddTaskDialog(
    onDismiss: () -> Unit,
    onAddTask: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Add Task") },
        text = {
            Column {
                TextField(value = name, onValueChange = { name = it }, label = { Text("Task Name") })
                TextField(value = startTime, onValueChange = { startTime = it }, label = { Text("Start Time") })
                TextField(value = endTime, onValueChange = { endTime = it }, label = { Text("End Time") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onAddTask(name, startTime, endTime)
                onDismiss()
            }) {
                Text("Add")
            }
        },
        dismissButton = {
            Button(onClick = onDismiss) {
                Text("Cancel")
            }
        }
    )
}

@Composable
fun EditTaskDialog(
    taskName: String,
    taskStartTime: String,
    taskEndTime: String,
    onDismiss: () -> Unit,
    onSave: (String, String, String) -> Unit
) {
    var name by remember { mutableStateOf(taskName) }
    var startTime by remember { mutableStateOf(taskStartTime) }
    var endTime by remember { mutableStateOf(taskEndTime) }

    AlertDialog(
        onDismissRequest = { onDismiss() },
        title = { Text(text = "Edit Task") },
        text = {
            Column {
                TextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Task Name") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = startTime,
                    onValueChange = { startTime = it },
                    label = { Text("Start Time") }
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = endTime,
                    onValueChange = { endTime = it },
                    label = { Text("End Time") }
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    onSave(name, startTime, endTime)
                    onDismiss()
                }
            ) {
                Text("Save")
            }
        },
        dismissButton = {
            Button(onClick = { onDismiss() }) {
                Text("Cancel")
            }
        }
    )
}
