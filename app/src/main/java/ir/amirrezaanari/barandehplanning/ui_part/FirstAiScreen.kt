package ir.amirrezaanari.barandehplanning.ui_part

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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite

@Composable
fun AiFirstScreen(){

    Column(
        modifier = Modifier.fillMaxSize()
            .padding(top = 60.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "نظر هوش\u200Cمصنوعی درباره برنامه\u200Cت چیه؟",
            fontSize = 20.sp
            )
        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = {},
            shape = RoundedCornerShape(25),
            modifier = Modifier
                .height(80.dp)
                .width(220.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = mainwhite
            ),
        ) {
            Text(
                text = "شروع چت با هوشیار",
                fontSize = 17.sp
                )
        }
        Spacer(modifier = Modifier.weight(1f))
        Image(
            modifier = Modifier.size(330.dp),
            painter = painterResource(id = R.drawable.robot_planning),
            contentDescription = "AiFirstScreen Image"
        )
    }
}