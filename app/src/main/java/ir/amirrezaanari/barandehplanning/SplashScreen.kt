package ir.amirrezaanari.barandehplanning

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandHorizontally
import androidx.compose.animation.fadeIn
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import kotlinx.coroutines.delay
@Composable
fun SplashScreen(navController: NavController) {
    var isVisibleLogo by remember { mutableStateOf(false) }
    var isVisibleText by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(300)
        isVisibleLogo = true
        delay(600)
        isVisibleText = true
        delay(1000)
        isLoading = false
        navController.navigate("main_screen") {
            popUpTo("splash_screen") { inclusive = true }
            launchSingleTop = true
        }
    }

    Box(
        modifier = Modifier.fillMaxSize().background(primary),
        contentAlignment = Alignment.Center
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(0.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            AnimatedVisibility(visible = isVisibleLogo, enter = fadeIn()) {
                Icon(
                    modifier = Modifier.size(110.dp),
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = "App Icon",
                    tint = Color.Unspecified
                )
            }
            AnimatedVisibility(visible = isVisibleText, enter = expandHorizontally()) {
                Column(
                    modifier = Modifier,
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.spacedBy(0.dp)
                ) {
                    Text(
                        text = "بــــــــرنـــــــده",
                        fontWeight = FontWeight.Black,
                        fontSize = 50.sp,
                        color = mainwhite,
                    )
                    Text(
                        text = "برنامه ریزی آینده",
                        fontSize = 31.sp,
                        color = mainwhite,
                    )
                }
            }
        }
    }
}
