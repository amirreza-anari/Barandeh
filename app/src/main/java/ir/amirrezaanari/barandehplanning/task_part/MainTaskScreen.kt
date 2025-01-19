package ir.amirrezaanari.barandehplanning.task_part

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import ir.amirrezaanari.barandehplanning.task_part.components.SectionFilterChip
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.red

@Composable
fun MainPlannerScreen(viewModel: PlannerViewModel) {
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }
    var isPlannedSection by remember { mutableStateOf(true) }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val plannedTasks by viewModel.plannedTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    Scaffold(
        floatingActionButton = {
            FloatingActionButton(
                onClick = { showAddBottomSheet = true },
                containerColor = mainwhite,
                contentColor = primary
            ) {
                Icon(
                    Icons.Rounded.Add,
                    contentDescription = "Add task",
                    tint = primary,
                    modifier = Modifier.size(35.dp)
                )
            }
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            DateSelector(
                selectedDate = selectedDate,
                onDateSelected = { viewModel.selectDate(it) }
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 12.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {
                SectionFilterChip(
                    selected = isPlannedSection,
                    onClick = { isPlannedSection = true },
                    label = "برنامه\u200Cهای ریخته\u200Cشده",
                    borderCondition = isPlannedSection,
                    modifier = Modifier
                        .fillMaxWidth(0.5f)
                        .height(40.dp)
                        .padding(horizontal = 2.dp),
                )
                SectionFilterChip(
                    selected = !isPlannedSection,
                    onClick = { isPlannedSection = false },
                    label = "برنامه\u200Cهای انجام\u200Cشده",
                    borderCondition = !isPlannedSection,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(40.dp)
                        .padding(horizontal = 2.dp),
                )
            }

            Spacer(modifier = Modifier.height(5.dp))
            if (isPlannedSection) {
                LazyColumn(
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(plannedTasks) { task ->
                        TaskItem(
                            task = task,
                            onTaskClick = {
                                selectedTask = it
                                showEditBottomSheet = true
                            }
                        )
                    }
                }
            } else {
                if (completedTasks.isEmpty()) {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        Button(
                            onClick = { viewModel.copyAllPlannedToCompleted() },
                            colors = ButtonDefaults.buttonColors(
                                containerColor = green,
                                contentColor = mainwhite
                            ),
                            shape = RoundedCornerShape(25)
                        ) {
                            Text("کپی برنامه‌های ریخته‌شده به انجام‌شده")
                        }
                    }
                } else {
                    LazyColumn(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(completedTasks) { task ->
                            TaskItem(
                                task = task,
                                onTaskClick = {
                                    selectedTask = it
                                    showEditBottomSheet = true
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    if (showAddBottomSheet) {
        AddTaskBottomSheet(
            viewModel = viewModel,
            onDismiss = { showAddBottomSheet = false },
            isPlanned = isPlannedSection
        )
    }
    if (showEditBottomSheet && selectedTask != null) {
        EditTaskBottomSheet(
            task = selectedTask!!,
            viewModel = viewModel,
            onDismiss = { showEditBottomSheet = false },
            onTaskDeleted = {
                selectedTask = null
            }
        )
    }
}