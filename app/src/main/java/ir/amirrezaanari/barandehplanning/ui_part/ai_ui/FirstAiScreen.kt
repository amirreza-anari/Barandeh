package ir.amirrezaanari.barandehplanning.ui_part.ai_ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.AiViewModel
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import kotlinx.coroutines.launch

@Composable
fun AiFirstScreen(navController: NavHostController, viewModel: AiViewModel) {

    val coroutineScope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(top = 100.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = """
                نظر هوش‌‌مصنوعی
                درباره برنامه‌ت چیه؟ 🤔
            """.trimIndent(),
            fontSize = 25.sp,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {
                coroutineScope.launch {
                    viewModel.clearResponse()
                    viewModel.getAiResponse()
                    navController.navigate("chat")
                }
            },
            shape = RoundedCornerShape(25),
            modifier = Modifier
                .height(80.dp)
                .width(220.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainwhite
            ),
        ) {
            Text(
                text = "نظر هوشیار چیه؟",
                fontSize = 17.sp,
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(370.dp),
            painter = painterResource(id = R.drawable.ai_thinking),
            contentDescription = "AiFirstScreen Image"
        )
    }
}