package ir.amirrezaanari.barandehplanning.task_part.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import ir.amirrezaanari.barandehplanning.ui_part.plan_ui.TimePicker
import java.time.LocalTime
import java.time.format.DateTimeFormatter

@Composable
fun TimePickerDialog(
    isopen: Boolean,
    onDismissRequest: () -> Unit,
    headerText: String,
    onClick: (String, Boolean) -> Unit,
    ){

    var dialogeTime by remember { mutableStateOf(LocalTime.now().format(DateTimeFormatter.ofPattern("HH:mm"))) }


    if (isopen){
        Dialog(
            onDismissRequest = onDismissRequest
        ) {
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
                        text = headerText,
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
                                dialogeTime = it.toString()
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
                            onClick(dialogeTime, false)
                        }
                    ) {
                        Text("تایید")
                    }
                }
            }
        }
    }
}