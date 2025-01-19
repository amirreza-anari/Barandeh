package ir.amirrezaanari.barandehplanning.ui_part.ai_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.task_part.PlannerViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.CustomFontFamily
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import kotlinx.coroutines.launch
import java.net.URLEncoder

@Composable
fun AiFirstScreen(navController: NavHostController, viewModel: PlannerViewModel) {

    val coroutineScope = rememberCoroutineScope()
    val dayPicker = remember { (1..30).map { it.toString().toPersianDigits() } }
    val dayPickerState = rememberPickerState()
    var statistics by remember { mutableStateOf("") }
//    val aiPickerState = rememberPickerState()

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxHeight(0.5f)
                .padding(vertical = 50.dp, horizontal = 30.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally

        ){
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .align(Alignment.CenterHorizontally),
                colors = CardDefaults.cardColors(
                    containerColor = secondary
                ),
                shape = RoundedCornerShape(15),
//                border = BorderStroke(1.dp, mainwhite)
            ) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "از هوشیار بپرس! 😁",
                        fontSize = 28.sp,
                        textAlign = TextAlign.Center,
                    )
                }
            }
//        Spacer(modifier = Modifier.height(100.dp))
//
//        Row(
//            Modifier
//                .fillMaxWidth(),
//            verticalAlignment = Alignment.CenterVertically,
//            horizontalArrangement = Arrangement.SpaceAround
//        ) {
//            Text("کدوم هوش مصنوعی؟")
//
//            AiPicker(
//                state = aiPickerState,
//                items = AiModels,
//                modifier = Modifier
//                    .height(120.dp)
//                    .width(140.dp)
//            )
//        }

//        Spacer(modifier = Modifier.height(75.dp))

            Row(
                Modifier
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceAround
            ) {
                Text(
                    text = "آمار چند روز گذشته؟",
                    fontSize = 18.sp
                )

                Picker(
                    modifier = Modifier
                        .width(50.dp),
                    items = dayPicker,
                    state = dayPickerState,
                    textModifier = Modifier.padding(top = 7.dp),
                    textStyle = TextStyle(
                        fontFamily = CustomFontFamily,
                        fontSize = 23.sp
                    )
                )
            }
        }
//        Spacer(Modifier.weight(1f))

        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Image(
                modifier = Modifier.fillMaxWidth(),
                painter = painterResource(id = R.drawable.ai_chat),
                contentDescription = "AiFirstScreen Image"
            )
            Box(
                modifier = Modifier.fillMaxWidth(0.65f).padding(bottom = 220.dp)
            ) {
                Button(
                    onClick = {
                        coroutineScope.launch {

                            val selectedDays = dayPickerState.selectedItem.toInt()

                            statistics = viewModel.getLastNDaysStatistics(selectedDays)

                            val encodedStatistics = URLEncoder.encode(statistics, "UTF-8")
                                .replace("+", "%20")

                            navController.navigate("chat/$encodedStatistics")
                        }
                    },
                    shape = RoundedCornerShape(25),
                    modifier = Modifier
                        .height(80.dp)
                        .fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = mainwhite
                    ),
                ) {
                    Text(
                        text = "بزن بریم!",
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
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