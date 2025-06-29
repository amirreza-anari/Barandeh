package ir.amirrezaanari.barandehplanning.planning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.CopyAll
import androidx.compose.material.icons.rounded.Keyboard
import androidx.compose.material.icons.rounded.Mic
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.AbsoluteAlignment
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.planning.components.DateSelector
import ir.amirrezaanari.barandehplanning.planning.components.SectionFilterChip
import ir.amirrezaanari.barandehplanning.planning.components.TaskItem
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity
import ir.amirrezaanari.barandehplanning.planning.voicetask.AddVoiceTaskBottomSheet
import ir.amirrezaanari.barandehplanning.planning.voicetask.VoiceProcessingState
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.red
import ir.amirrezaanari.barandehplanning.ui.theme.secondary

@Composable
fun MainPlannerScreen(viewModel: PlannerViewModel) {
    var showAddBottomSheet by remember { mutableStateOf(false) }
    var showEditBottomSheet by remember { mutableStateOf(false) }
    var showAddVoiceTaskBottomSheet by remember { mutableStateOf(false) }
    var selectedTask by remember { mutableStateOf<TaskEntity?>(null) }
    var isPlannedSection by remember { mutableStateOf(true) }

    val selectedDate by viewModel.selectedDate.collectAsState()
    val plannedTasks by viewModel.plannedTasks.collectAsState()
    val completedTasks by viewModel.completedTasks.collectAsState()

    val voiceProcessingState by viewModel.voiceProcessingState.collectAsState()


    var isAddClicked by remember { mutableStateOf(false) }
    Scaffold(
        floatingActionButton = {
            ExpandingFloatingActionButton(
                isExpanded = isAddClicked,
                onMainFabClick = { isAddClicked = !isAddClicked },
                onKeyboardFabClick = { showAddBottomSheet = true },
                onMicFabClick = { showAddVoiceTaskBottomSheet = true }
            )
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

            Spacer(modifier = Modifier.height(8.dp))
            AnimatedContent(
                targetState = isPlannedSection,
                transitionSpec = {
                    fadeIn(animationSpec = tween(200)) togetherWith fadeOut(
                        animationSpec = tween(
                            300
                        )
                    )
                },
                label = "task list"
            ) { targetIsPlanned ->
                if (targetIsPlanned) {
                    if (plannedTasks.isNotEmpty()) {
                        LazyColumn(modifier = Modifier.fillMaxSize()) {
                            items(plannedTasks) { task ->
                                TaskItem(
                                    task = task,
                                    onTaskClick = {
                                        selectedTask = it
                                        showEditBottomSheet = true
                                    },
                                    onCheckChange = { taskTick ->
                                        viewModel.toggleTaskCheckStatus(taskTick)
                                    }
                                )
                            }
                        }
                    } else {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.emptybox),
                                contentDescription = "empty tasks",
                                modifier = Modifier.fillMaxWidth(0.6f)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text("هیچ برنامه ای این نریختی هنوز! ☹️")
                        }
                    }
                } else {
                    if (plannedTasks.isEmpty()) {
                        Column(
                            modifier = Modifier
                                .fillMaxSize(),
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Image(
                                painter = painterResource(R.drawable.emptybox),
                                contentDescription = "empty tasks",
                                modifier = Modifier.fillMaxWidth(0.6f)
                            )
                            Spacer(Modifier.height(10.dp))
                            Text("هیچ برنامه ای رو انجام ندادی هنوز! ☹️")
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
                                        contentColor = primary
                                    ),
                                    modifier = Modifier
                                        .fillMaxWidth(0.8f)
                                        .height(50.dp),
                                    shape = RoundedCornerShape(25)
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxSize(),
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center
                                    ) {
                                        Icon(
                                            Icons.Rounded.CopyAll,
                                            contentDescription = "copy plannedTasks to completedTasks",
                                        )
                                        Spacer(Modifier.width(8.dp))
                                        Text(
                                            text = "کپی برنامه‌های ریخته‌شده به انجام‌شده",
                                        )
                                    }
                                }
                            }
                        } else {
                            LazyColumn(modifier = Modifier.fillMaxSize()) {
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
    if (showAddVoiceTaskBottomSheet){
        AddVoiceTaskBottomSheet(
            viewModel = viewModel,
            onDismiss = { showAddVoiceTaskBottomSheet = false}
        )
    }

    VoiceProcessingDialog(
        state = voiceProcessingState,
        onDismissError = { viewModel.resetVoiceProcessingState() }
    )
}

@Composable
fun ExpandingFloatingActionButton(
    isExpanded: Boolean,
    onMainFabClick: () -> Unit,
    onKeyboardFabClick: () -> Unit,
    onMicFabClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = AbsoluteAlignment.Left
    ) {
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + slideInVertically(initialOffsetY = { it / 2 }),
            exit = fadeOut() + slideOutVertically(targetOffsetY = { it / 2 })
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = AbsoluteAlignment.Left
            ) {
                FloatingActionButton(
                    onClick = onMicFabClick,
                    containerColor = mainwhite,
                    contentColor = primary
                ) {
                    Icon(
                        Icons.Rounded.Mic,
                        contentDescription = "Add task by voice",
                        tint = primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
                FloatingActionButton(
                    onClick = onKeyboardFabClick,
                    containerColor = mainwhite,
                    contentColor = primary
                ) {
                    Icon(
                        Icons.Rounded.Keyboard,
                        contentDescription = "Add task by keyboard",
                        tint = primary,
                        modifier = Modifier.size(25.dp)
                    )
                }
            }
        }

        Spacer(Modifier.height(8.dp))
        val fabContainerColor by animateColorAsState(
            targetValue = if (isExpanded) red else mainwhite,
            animationSpec = tween(durationMillis = 200),
            label = "fabContainerColor"
        )

        val fabIconColor by animateColorAsState(
            targetValue = if (isExpanded) mainwhite else primary,
            animationSpec = tween(durationMillis = 200),
            label = "fabIconColor"
        )

        val rotationAngle by animateFloatAsState(
            targetValue = if (isExpanded) 45f else 0f,
            animationSpec = tween(durationMillis = 200),
            label = "fabRotation"
        )

        FloatingActionButton(
            onClick = onMainFabClick,
            containerColor = fabContainerColor,
            contentColor = primary
        ) {
            Icon(
                imageVector = Icons.Rounded.Add,
                contentDescription = "Toggle Add Task",
                tint = fabIconColor,
                modifier = Modifier
                    .size(40.dp)
                    .rotate(rotationAngle)
            )
        }
    }
}

@Composable
fun VoiceProcessingDialog(
    state: VoiceProcessingState,
    onDismissError: () -> Unit
) {
    if (state is VoiceProcessingState.Loading || state is VoiceProcessingState.Error) {
        Dialog(
            onDismissRequest = { if (state is VoiceProcessingState.Error) onDismissError() }
        ) {
            Card(
                shape = RoundedCornerShape(16.dp),
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {
                    when (state) {
                        is VoiceProcessingState.Loading -> {
                            CircularProgressIndicator(
                                color = red
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text("در حال پردازش...")
                        }
                        is VoiceProcessingState.Error -> {
                            Icon(
                                imageVector = Icons.Rounded.Close,
                                contentDescription = "Error",
                                tint = Color.Red,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "خطا",
                                style = MaterialTheme.typography.titleLarge,
                                fontWeight = FontWeight.Bold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(state.message, textAlign = TextAlign.Center)
                            Spacer(modifier = Modifier.height(24.dp))
                            Button(
                                onClick = onDismissError,
                                shape = RoundedCornerShape(15.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = mainwhite,
                                    contentColor = secondary
                                )
                            ) {
                                Text("متوجه شدم")
                            }
                        }
                        else -> {
                        }
                    }
                }
            }
        }
    }
}