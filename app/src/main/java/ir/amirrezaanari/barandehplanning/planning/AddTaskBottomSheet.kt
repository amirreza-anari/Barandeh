package ir.amirrezaanari.barandehplanning.planning

import android.widget.Toast
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.ColorLens
import androidx.compose.material.icons.rounded.TextFields
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.planning.components.CardColorPicker
import ir.amirrezaanari.barandehplanning.planning.components.CustomTextField
import ir.amirrezaanari.barandehplanning.planning.components.IconAndText
import ir.amirrezaanari.barandehplanning.planning.components.TimePickerDialog
import ir.amirrezaanari.barandehplanning.planning.components.TimePickerDialogButton
import ir.amirrezaanari.barandehplanning.planning.components.cardColors
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.planning.database.TaskEntity
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.red
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import java.time.LocalTime

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddTaskBottomSheet(
    viewModel: PlannerViewModel,
    onDismiss: () -> Unit,
    isPlanned: Boolean
) {
    var title by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("زمان شروع") }
    var endTime by remember { mutableStateOf("زمان پایان") }
    var details by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(red) }
    var isStartTimeDialogOpen by remember { mutableStateOf(false) }
    var isEndTimeDialogOpen by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    val context = LocalContext.current

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        containerColor = secondary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "افزودن برنامه نوشتاری",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            IconAndText(
                icon = Icons.Rounded.Title,
                text = "عنوان"
            )
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 5.dp),
                value = title,
                label = "عنوان برنامه رو اینجا بنویس :)",
                onValueChange = { title = it },
                singleline = true
            )

            Spacer(modifier = Modifier.height(10.dp))

            IconAndText(
                icon = Icons.Rounded.AccessTime,
                text = "زمان"
            )

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
            ) {
                TimePickerDialogButton(
                    modifier = Modifier.weight(2f),
                    onClick = { isStartTimeDialogOpen = true },
                    text = startTime
                )
                Text(
                    "تا",
                    modifier = Modifier.weight(1f),
                    textAlign = TextAlign.Center,
                    color = Color.White.copy(alpha = 0.7f)
                )
                TimePickerDialogButton(
                    modifier = Modifier.weight(2f),
                    onClick = { isEndTimeDialogOpen = true },
                    text = endTime
                )
            }

            IconAndText(
                icon = Icons.Rounded.TextFields,
                text = "جزئیات"
            )
            CustomTextField(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .padding(top = 5.dp),
                value = details,
                label = "جزئیات برنامه تو اینجا بنویس :)",
                onValueChange = {
                    details = it
                },
                singleline = false,
                maxlines = 5
            )

            IconAndText(
                icon = Icons.Rounded.ColorLens,
                text = "رنگ"
            )
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 5.dp)
            ) {
                items(cardColors) { cardcolor ->
                    CardColorPicker(
                        color = cardcolor.color,
                        onClick = { selectedColor = cardcolor.color },
                        isSelected = cardcolor.color == selectedColor
                    )

                }
            }



            Button(
                onClick = {
                    if (title.isBlank() || startTime == "زمان شروع" || endTime == "زمان پایان") {
                        Toast.makeText(
                            context,
                            "لطفاً عنوان، زمان شروع و زمان پایان را وارد کنید.",
                            Toast.LENGTH_SHORT
                        ).show()
                    } else {
                        val newTask = TaskEntity(
                            title = title,
                            startTime = startTime,
                            endTime = endTime,
                            details = details,
                            color = selectedColor.toArgb(),
                            isPlanned = isPlanned,
                            date = viewModel.selectedDate.value.toString()
                        )
                        viewModel.addTask(newTask)
                        onDismiss()
                    }
                },
                shape = RoundedCornerShape(8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(70.dp)
                    .padding(vertical = 12.dp),
                colors = ButtonDefaults.buttonColors(
                    contentColor = primary,
                    containerColor = mainwhite
                )
            ) {
                Text(
                    text = "افزودن",
                    fontSize = 16.sp
                )
            }
        }
        TimePickerDialog(
            onDismissRequest = { isEndTimeDialogOpen = false },
            onClick = { string, bool ->
                endTime = string
                isEndTimeDialogOpen = bool
            },
            headerText = "زمان پایان رو انتخاب کن!",
            isopen = isEndTimeDialogOpen,
            tasktime = LocalTime.now().toString()
        )
        TimePickerDialog(
            onDismissRequest = { isStartTimeDialogOpen = false },
            onClick = { string, bool ->
                startTime = string
                isStartTimeDialogOpen = bool
            },
            headerText = "زمان شروع رو انتخاب کن!",
            isopen = isStartTimeDialogOpen,
            tasktime = LocalTime.now().toString()

        )
    }
}

