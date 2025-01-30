package ir.amirrezaanari.barandehplanning.homescreen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.secondary

@Composable
fun PagesSection(navController: NavHostController){

    Row (
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(0.3f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceAround
    ){
        Card(
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .padding(end = 4.dp)
                .aspectRatio(1f),
            colors = CardDefaults.cardColors(
                containerColor = secondary
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = {navController.navigate("planning")}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Image(
                    painterResource(R.drawable.planhome),
                    contentDescription = "home screen planning image",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.3f)
                        .offset(x = (-45).dp, y = (35).dp)
                )

                Box(
                    modifier = Modifier
                        .offset(x = (45).dp, y = (30).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "برنامه ریزی",
                        color = secondary,
                        style = LocalTextStyle.current.copy(
                            drawStyle = Stroke(
                                width = 50f,
                                join = StrokeJoin.Round,
                            )
                        ),
                        fontSize = 25.sp,
                    )
                    Text(
                        text = "برنامه ریزی",
                        color = mainwhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
        Card(
            modifier = Modifier
                .fillMaxSize()
                .padding(start = 4.dp)
                .aspectRatio(1f),
            colors = CardDefaults.cardColors(
                containerColor = secondary
            ),
            shape = RoundedCornerShape(15.dp),
            onClick = {navController.navigate("aiassistant")}
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                Image(
                    painterResource(R.drawable.assistanthome),
                    contentDescription = "home screen ai assistant image",
                    modifier = Modifier
                        .fillMaxSize()
                        .scale(1.3f)
                        .offset(x = (40).dp, y = (35).dp)
                )

                Box(
                    modifier = Modifier
                        .offset(x = (20).dp, y = (30).dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "هوشیار",
                        color = secondary,
                        style = LocalTextStyle.current.copy(
                            drawStyle = Stroke(
                                width = 50f,
                                join = StrokeJoin.Round,
                            )
                        ),
                        fontSize = 25.sp,
                    )
                    Text(
                        text = "هوشیار",
                        color = mainwhite,
                        fontSize = 25.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }
        }
    }
}