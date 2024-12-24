package ir.amirrezaanari.barandehplanning.ui_part.plan_ui

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.animation.togetherWith
import androidx.compose.animation.with
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.database.CompletedTask
import ir.amirrezaanari.barandehplanning.database.PlannedTask
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.ui_part.AddTaskDialog
import ir.amirrezaanari.barandehplanning.ui_part.EditTaskDialog
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.style.TextAlign
import androidx.navigation.NavController
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import okhttp3.internal.concurrent.Task


@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TaskPlannerScreen(viewModel: TaskViewModel, navController: NavController) {
    var selectedTab by remember { mutableStateOf(0) }
    var selectedTask by remember { mutableStateOf<Task?>(null) }

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("add_task_screen/$selectedTab") },
                containerColor = mainwhite
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add Task", tint = primary)
            }
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(10.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                FilterChip(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    label = {
                        Text(
                            text = "برنامه\u200Cهای ریخته\u200Cشده",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(40.dp)
                        .padding(horizontal = 2.dp),
                    shape = MaterialTheme.shapes.small,
                    border = if (selectedTab == 0) null else BorderStroke(
                        1.dp,
                        SolidColor(mainwhite)
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = mainwhite,
                        selectedLabelColor = primary,
                        containerColor = Color.Transparent,
                        labelColor = mainwhite,
                    )
                )

                FilterChip(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    label = {
                        Text(
                            text = "برنامه\u200Cهای عمل\u200Cشده",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.Center,
                        )
                            },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 2.dp),
                    shape = MaterialTheme.shapes.small,
                    border = if (selectedTab == 1) null else BorderStroke(
                        1.dp,
                        SolidColor(mainwhite)
                    ),
                    colors = FilterChipDefaults.filterChipColors(
                        selectedContainerColor = mainwhite,
                        selectedLabelColor = primary,
                        containerColor = Color.Transparent,
                        labelColor = mainwhite,
                    )
                )
            }
//            AnimatedContent(
//                targetState = selectedTab,
//                transitionSpec =
//            ) { }
            AnimatedContent(
                targetState = selectedTab,
                transitionSpec = {
                    if (targetState == 0) {
                        // حرکت به سمت "برنامه‌های عمل‌شده"
//                        fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
                        (fadeIn() + scaleIn(initialScale = 0.5f)).togetherWith(fadeOut())

                    } else {
                        // حرکت به سمت "برنامه‌های ریخته‌شده"
//                        fadeIn(animationSpec = tween(500)) with fadeOut(animationSpec = tween(500))
                        (fadeIn() + scaleIn(initialScale = 0.5f)).togetherWith(fadeOut())

                    }
                }
            ) { tab ->
                when (tab) {
                    0 -> PlannedTasksScreen(viewModel)
                    1 -> CompletedTasksScreen(viewModel)
                }
            }
//            when (selectedTab) {
//                0 -> PlannedTasksScreen(viewModel)
//                1 -> CompletedTasksScreen(viewModel)
//            }
        }
    }
}


@Composable
fun PlannedTasksScreen(viewModel: TaskViewModel) {
    val plannedTasks by viewModel.plannedTasks.collectAsState(initial = emptyList())
    var selectedTask by remember { mutableStateOf<PlannedTask?>(null) }

    LazyColumn {
        items(plannedTasks) { task ->
            TaskItem(
                starttime = task.startTime,
                name = task.name,
                endtime = task.endTime,
                onClick = {
                    selectedTask = task // ذخیره‌ی تسک انتخاب‌شده
                }
            )
        }
    }
}

@Composable
fun CompletedTasksScreen(viewModel: TaskViewModel) {
    val completedTasks by viewModel.completedTasks.collectAsState(initial = emptyList())
    var selectedTask by remember { mutableStateOf<CompletedTask?>(null) }

    LazyColumn {
        items(completedTasks) { task ->
            TaskItem(
                starttime = task.startTime,
                name = task.name,
                endtime = task.endTime,
                onClick = {
                    selectedTask = task // ذخیره‌ی تسک انتخاب‌شده
                }
            )
        }
    }
}