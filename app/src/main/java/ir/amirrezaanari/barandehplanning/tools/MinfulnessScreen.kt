package ir.amirrezaanari.barandehplanning.tools

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.KeyboardArrowDown
import androidx.compose.material.icons.rounded.KeyboardArrowUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.planning.components.toPersianDigits
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.red
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class BreathingPhase(val duration: Int, val instruction: String) {
    INHALING(4, "نفس بگیر"),
    HOLDING_FULL(4, "نفست رو حبس کن"),
    EXHALING(4, "نفست رو بده بیرون"),
    HOLDING_EMPTY(4, "حبس کن"),
    IDLE(0, "برای شروع ضربه بزن")
}

@Composable
fun MindfulnessScreen() {
    var currentPhase by remember { mutableStateOf(BreathingPhase.IDLE) }
    var remainingTime by remember { mutableIntStateOf(0) }
    var isRunning by remember { mutableStateOf(false) }
    var cyclesCompleted by remember { mutableIntStateOf(1) }
    var totalCycles by remember { mutableIntStateOf(5) }
    val progress = remember { Animatable(1f) }

    LaunchedEffect(key1 = isRunning) {
        if (isRunning) {
            for (cycle in 1..totalCycles) {
                if (!isRunning) break
                cyclesCompleted = cycle
                val boxBreathingCycle = listOf(
                    BreathingPhase.INHALING,
                    BreathingPhase.HOLDING_FULL,
                    BreathingPhase.EXHALING,
                    BreathingPhase.HOLDING_EMPTY
                )

                for (phase in boxBreathingCycle) {
                    if (!isRunning) break
                    currentPhase = phase
                    progress.snapTo(1f)
                    launch {
                        progress.animateTo(
                            targetValue = 0f,
                            animationSpec = tween(
                                durationMillis = phase.duration * 1000,
                                easing = LinearEasing
                            )
                        )
                    }
                    for (time in phase.duration downTo 0) {
                        if (!isRunning) break
                        remainingTime = time
                        if (time > 0) {
                            delay(1000L)
                        }
                    }
                }
            }
            isRunning = false
        } else {
            currentPhase = BreathingPhase.IDLE
            progress.snapTo(1f)
            remainingTime = 0
            cyclesCompleted = 1
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "تمرینات تنفس برای تازه سازی ذهن با استفاده از روش تنفس جعبه ای 🧠",
            textAlign = TextAlign.Center,
            fontSize = 18.sp,
            color = MaterialTheme.colorScheme.onSurface
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 20.dp)
                .alpha(if (isRunning) 0.5f else 1f),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            Text(
                text = "تعداد چرخه تمرین:"
            )
            Spacer(Modifier.width(16.dp))
            IconButton(
                onClick = { if (totalCycles > 1) totalCycles-- },
                enabled = !isRunning
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowDown,
                    contentDescription = "decrease cycle number"
                )
            }
            Text(
                text = totalCycles.toString().toPersianDigits(),
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.width(40.dp),
                textAlign = TextAlign.Center
            )
            IconButton(
                onClick = { if (totalCycles < 20) totalCycles++ },
                enabled = !isRunning
            ) {
                Icon(
                    Icons.Rounded.KeyboardArrowUp,
                    contentDescription = "increase cycle number"
                )
            }
        }

//        Spacer(Modifier.height(16.dp))


        Text(
            text = if (isRunning) {
                "چرخه ${
                    cyclesCompleted.toString().toPersianDigits()
                } از ${totalCycles.toString().toPersianDigits()}"
            } else {
                ""
            },
            fontSize = 18.sp,
            color = mainwhite,
            textAlign = TextAlign.Center,
        )
        Spacer(Modifier.height(25.dp))

        Text(
            text = currentPhase.instruction,
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = mainwhite,
            textAlign = TextAlign.Center,
        )
        Box(
            modifier = Modifier
                .fillMaxWidth(0.8f)
                .fillMaxHeight()
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxSize(),
                color = Color(0xFF00BCD4),
                strokeWidth = 12.dp,
                trackColor = Color(0x33FFFFFF),
                strokeCap = StrokeCap.Square
            )
            Button(
                onClick = { isRunning = !isRunning }, // Simplified onClick
                modifier = Modifier
                    .fillMaxSize()
                    .padding(15.dp),
                shape = CircleShape,
                colors = ButtonDefaults.buttonColors(
                    containerColor = red,
                    contentColor = mainwhite
                )
            ) {
                Text(
                    text = if (isRunning) remainingTime.toString().toPersianDigits() else "شروع",
                    fontSize = if (isRunning) 80.sp else 40.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}