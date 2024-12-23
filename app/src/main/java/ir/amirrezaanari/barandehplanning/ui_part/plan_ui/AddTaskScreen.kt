package ir.amirrezaanari.barandehplanning.ui_part.plan_ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.rounded.AccessTime
import androidx.compose.material.icons.rounded.Title
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.amirrezaanari.barandehplanning.database.PlannedTask
import ir.amirrezaanari.barandehplanning.database.TaskViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import java.time.LocalTime

@Composable
fun AddTaskScreen(viewModel: TaskViewModel, navController: NavController) {
    var taskName by remember { mutableStateOf("") }
    var startTime by remember { mutableStateOf("") }
    var endTime by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(primary)
            .padding(16.dp)
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

        Spacer(modifier = Modifier.height(25.dp))

        Row(
            modifier = Modifier,
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                imageVector = Icons.Rounded.Title,
                contentDescription = "Title Icon",
                tint = mainwhite,
                modifier = Modifier.size(24.dp)
            )
            Text("عنوان", fontSize = 15.sp, modifier = Modifier.padding(start = 5.dp))

        }
        OutlinedTextField(
            modifier = Modifier.fillMaxWidth(),
            value = taskName,
            label = { Text(text = "عنوان برنامه") },
            onValueChange = {
                taskName = it
            }
        )

        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
        ) {
            Icon(
                imageVector = Icons.Rounded.AccessTime,
                contentDescription = "Time Icon",
                tint = mainwhite,
                modifier = Modifier.size(24.dp)
            )
            Text("زمان", fontSize = 15.sp, modifier = Modifier.padding(start = 5.dp))
        }
        Row(
            modifier = Modifier.padding(top = 10.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            //one here
                TimePicker(
                    currentTime = LocalTime.now(),
                    is24TimeFormat = true,
                    onTimeChanged = {}
                )

            //second here
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                viewModel.addPlannedTask(
                    PlannedTask(name = taskName, startTime = startTime, endTime = endTime)
                )
            },
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier
                .fillMaxWidth(),
        ) {
            Text("Save Task")
        }
    }
}
