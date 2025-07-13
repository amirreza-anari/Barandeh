package ir.amirrezaanari.barandehplanning.planning.components

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TimePicker
import androidx.compose.material3.TimePickerDefaults
import androidx.compose.material3.rememberTimePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TimePickerDialog(
    isopen: Boolean,
    onDismissRequest: () -> Unit,
    headerText: String,
    onClick: (String, Boolean) -> Unit,
    tasktime: String
) {
    // Parse initial time or use current time if tasktime is default
    val initialTime = remember {
        try {
            if (tasktime == "زمان شروع" || tasktime == "زمان پایان") {
                LocalTime.now()
            } else {
                LocalTime.parse(tasktime)
            }
        } catch (e: Exception) {
            LocalTime.now()
        }
    }

    val timeState = rememberTimePickerState(
        initialHour = initialTime.hour,
        initialMinute = initialTime.minute,
        is24Hour = true
    )

    if (isopen) {
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(
                    containerColor = secondary
                )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    Text(
                        text = headerText,
                        modifier = Modifier.padding(vertical = 16.dp),
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(Modifier.height(25.dp))
                    TimePicker(
                        state = timeState,
                        colors = TimePickerDefaults.colors(
                            clockDialColor = primary,
                            clockDialSelectedContentColor = primary,
                            clockDialUnselectedContentColor = mainwhite,
                            selectorColor = mainwhite,
                            timeSelectorSelectedContentColor = primary,
                            timeSelectorSelectedContainerColor = mainwhite,
                            timeSelectorUnselectedContentColor = mainwhite,
                            timeSelectorUnselectedContainerColor = primary,
                        ),
                    )
                    Spacer(Modifier.height(25.dp))
                    Row(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                val formattedTime = formattedTime(timeState.hour, timeState.minute)
                                onClick(formattedTime, false)
                            },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = mainwhite,
                                containerColor = green
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("تایید")
                        }
                        Spacer(Modifier.width(10.dp))
                        Button(
                            onClick = { onDismissRequest() },
                            modifier = Modifier.weight(1f),
                            colors = ButtonDefaults.buttonColors(
                                contentColor = primary,
                                containerColor = mainwhite
                            ),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Text("لغو")
                        }
                    }
                }
            }
        }
    }
}

fun formattedTime(hour: Int, minute: Int): String {
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return LocalTime.of(hour, minute).format(formatter)
}