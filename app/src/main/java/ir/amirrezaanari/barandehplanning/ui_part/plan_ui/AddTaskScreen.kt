package ir.amirrezaanari.barandehplanning.ui_part.plan_ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.navigation.NavController
import ir.amirrezaanari.barandehplanning.database.CompletedTask
import ir.amirrezaanari.barandehplanning.database.PlannedTask
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.database.toTime
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController, selectedTab: Int) {
    val focusManager = LocalFocusManager.current
    var taskName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("زمان شروع") }
    var endTime by remember { mutableStateOf("زمان پایان") }
    var isStartTimeDialogOpen by remember { mutableStateOf(false) }
    var isEndTimeDialogOpen by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .padding(16.dp)
            .clickable(
                onClick = { focusManager.clearFocus()},
                indication = null, // حذف indicator
                interactionSource = remember { MutableInteractionSource() },
            )
    ) {
        Card(
            modifier = Modifier.fillMaxWidth()
                .fillMaxHeight(0.1f),
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(
                containerColor = secondary
            )
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Rounded.ArrowBackIos,
                    contentDescription = "Home Icon",
                    tint = mainwhite,
                    modifier = Modifier.size(30.dp)
                        .clickable { navController.popBackStack() }
                )
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("افزودن برنامه", fontSize = 20.sp)
                }
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Rounded.Title,
                contentDescription = "Title Icon",
                tint = mainwhite.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Text(
                "عنوان",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 5.dp),
                color = Color.White.copy(alpha = 0.7f)

            )

        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth().padding(top = 5.dp),
            value = taskName,
            label = { Text(text = "عنوان برنامه رو اینجا بنویس ;)") },
            onValueChange = {
                taskName = it
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = ImeAction.Done // تنظیم عمل کیبورد روی Done
            ),
            keyboardActions = KeyboardActions(
                onDone = { focusManager.clearFocus() } // هنگام فشردن Done
            ),
            shape = RoundedCornerShape(8.dp),
            colors = TextFieldDefaults.colors(
                unfocusedContainerColor = Color.Transparent,
                unfocusedLabelColor = mainwhite,
                focusedContainerColor = Color.Transparent,
                unfocusedPlaceholderColor = mainwhite,
                unfocusedIndicatorColor = mainwhite,
                unfocusedTextColor = mainwhite,
                unfocusedPrefixColor = mainwhite,
                unfocusedSuffixColor = mainwhite,
                unfocusedLeadingIconColor = mainwhite,
                unfocusedTrailingIconColor = mainwhite,
                unfocusedSupportingTextColor = mainwhite,
                focusedIndicatorColor = mainwhite,
                focusedTextColor = mainwhite,
                focusedLabelColor = mainwhite,
                focusedPlaceholderColor = mainwhite,

            )
        )

        Row(
            modifier = Modifier.padding(top = 25.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = "Time Icon",
                tint = mainwhite.copy(alpha = 0.7f),
                modifier = Modifier.size(24.dp)
            )
            Text(
                "زمان",
                fontSize = 15.sp,
                modifier = Modifier
                    .padding(start = 5.dp),
                color = Color.White.copy(alpha = 0.7f)
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ){
            Button(
                modifier = Modifier.weight(2f),
                onClick = { isStartTimeDialogOpen = true },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, mainwhite),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = mainwhite
                )
            ) {
                Text(startTime.toPersianDigits())
            }

            if (isStartTimeDialogOpen) {
                Dialog(onDismissRequest = { isStartTimeDialogOpen = false }) {
                    var dialogeStartTime by remember { mutableStateOf(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm"))) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = secondary
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ){
                            Text(
                                text = "زمان شروع رو انتخاب کن!",
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.8f),
                                contentAlignment = Alignment.Center
                            ) {
                                TimePicker(
                                    is24TimeFormat = true,
                                    currentTime = LocalTime.now(),
                                    onTimeChanged = {
                                        dialogeStartTime = it.toString()
                                    },
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = primary,
                                    containerColor = mainwhite
                                ),
                                onClick = {
                                    isStartTimeDialogOpen = false
                                    startTime = dialogeStartTime.toString()
                                }
                            ) {
                                Text("تایید")
                            }
                        }

                    }
                }
            }


            Text("تا",
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f)

                )
            Button(
                modifier = Modifier.weight(2f),
                onClick = { isEndTimeDialogOpen = true },
                shape = RoundedCornerShape(8.dp),
                border = BorderStroke(1.dp, mainwhite),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent,
                    contentColor = mainwhite
                )
            ) {
                Text(endTime.toPersianDigits())
            }

            if (isEndTimeDialogOpen) {
                Dialog(onDismissRequest = { isEndTimeDialogOpen = false }) {

                    var dialogeEndTime by remember { mutableStateOf(LocalTime.now().format(
                        DateTimeFormatter.ofPattern("HH:mm"))) }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .fillMaxHeight(0.5f),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = secondary
                        )
                    ) {
                        Column(
                            modifier = Modifier.fillMaxSize()
                                .padding(10.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.SpaceEvenly
                        ){
                            Text(
                                text = "زمان پایان رو انتخاب کن!",
                                modifier = Modifier
                                    .padding(vertical = 10.dp)
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxHeight(0.8f),
                                contentAlignment = Alignment.Center
                            ) {
                                TimePicker(
                                    is24TimeFormat = true,
                                    currentTime = LocalTime.now(),
                                    onTimeChanged = {
                                        dialogeEndTime = it.toString()
                                    },
                                )
                            }

                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    contentColor = primary,
                                    containerColor = mainwhite
                                ),
                                onClick = {
                                    isEndTimeDialogOpen = false
                                    endTime = dialogeEndTime.toString()
                                }
                            ) {
                                Text("تایید")
                            }
                        }

                    }
                }
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                if (selectedTab == 0) {
                    viewModel.addPlannedTask(
                        PlannedTask(name = taskName, startTime = startTime, endTime = endTime)
                    )
                } else {
                    viewModel.addCompletedTask(
                        CompletedTask(name = taskName, startTime = startTime, endTime = endTime)
                    )
                }
                navController.popBackStack()
            },
            shape = RoundedCornerShape(8.dp),
            modifier = Modifier
                .fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(
                contentColor = primary,
                containerColor = mainwhite
            )
        ) {
            Text("تایید", fontSize = 17.sp)
        }
    }
}

fun String.toPersianDigits(): String {
    val englishDigits = '0'..'9'
    val persianDigits = listOf('۰', '۱', '۲', '۳', '۴', '۵', '۶', '۷', '۸', '۹')
    return this.map { char ->
        if (char in englishDigits) {
            persianDigits[char - '0']
        } else {
            char
        }
    }.joinToString("")
}
