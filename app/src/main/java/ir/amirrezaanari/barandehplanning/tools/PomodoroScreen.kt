package ir.amirrezaanari.barandehplanning.tools

import android.content.Context
import android.media.MediaPlayer
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.rounded.ArrowBackIos
import androidx.compose.material.icons.automirrored.rounded.VolumeOff
import androidx.compose.material.icons.automirrored.rounded.VolumeUp
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material.icons.rounded.MusicOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material.icons.rounded.VolumeOff
import androidx.compose.material.icons.rounded.VolumeUp
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.planning.components.toPersianDigits
import ir.amirrezaanari.barandehplanning.ui.theme.cyan
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.red
import ir.amirrezaanari.barandehplanning.ui.theme.secondary
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

private enum class PomodoroPhase(val durationInSeconds: Int, val title: String) {
    FOCUS(25 * 60, "زمان تمرکز"),
    SHORT_BREAK(5 * 60, "استراحت کوتاه"),
    LONG_BREAK(15 * 60, "استراحت طولانی"),
    IDLE(0, "آماده برای شروع")
}

private fun Int.formatTime(): String {
    val minutes = this / 60
    val seconds = this % 60
    return "${minutes.toString().padStart(2, '0')}:${seconds.toString().padStart(2, '0')}"
}

@Composable
fun PomodoroScreen(
    title: String = "",
    details: String = "",
    onNavigateBack: () -> Unit,
    isFromTask: Boolean
) {
    // --- State Management ---
    var currentPhase by remember { mutableStateOf(PomodoroPhase.IDLE) }
    var remainingTime by remember { mutableStateOf(PomodoroPhase.FOCUS.durationInSeconds) }
    var isRunning by remember { mutableStateOf(false) }
    var pomodoroCount by remember { mutableStateOf(0) }
    val progress = remember { Animatable(1f) }
    val scope = rememberCoroutineScope()

    // --- Sound State ---
    var isAlarmSoundOn by remember { mutableStateOf(true) }
    var isMusicPlaying by remember { mutableStateOf(false) }
    val context = LocalContext.current

    // --- MediaPlayer Setup ---
    val alarmPlayer = remember { MediaPlayer.create(context, R.raw.pomodoro_alert) }
    val musicPlayer = remember {
        MediaPlayer.create(context, R.raw.pomodoro_music).apply {
            isLooping = true // Music should loop
        }
    }

    // --- Lifecycle Management for MediaPlayers ---
    DisposableEffect(Unit) {
        onDispose {
            alarmPlayer.release()
            musicPlayer.release()
        }
    }

    // --- Main Timer Logic ---
    LaunchedEffect(isRunning, currentPhase) {
        if (isRunning) {
            launch {
                progress.snapTo(remainingTime.toFloat() / currentPhase.durationInSeconds)
                progress.animateTo(
                    targetValue = 0f,
                    animationSpec = tween(
                        durationMillis = remainingTime * 1000,
                        easing = LinearEasing
                    )
                )
            }
            while (remainingTime > 0 && isRunning) {
                delay(1000L)
                remainingTime--
            }
            if (isRunning) {
                // Play alarm when a phase ends
                if (isAlarmSoundOn) {
                    alarmPlayer.start()
                }

                // Transition to the next phase
                when (currentPhase) {
                    PomodoroPhase.FOCUS -> {
                        pomodoroCount++
                        val nextPhase =
                            if (pomodoroCount % 4 == 0) PomodoroPhase.LONG_BREAK else PomodoroPhase.SHORT_BREAK
                        currentPhase = nextPhase
                        remainingTime = nextPhase.durationInSeconds
                    }
                    PomodoroPhase.SHORT_BREAK, PomodoroPhase.LONG_BREAK -> {
                        currentPhase = PomodoroPhase.FOCUS
                        remainingTime = PomodoroPhase.FOCUS.durationInSeconds
                    }
                    else -> {}
                }
                progress.snapTo(1f)
            }
        }
    }

    // --- Background Music Logic ---
    LaunchedEffect(isMusicPlaying, currentPhase) {
        // Only play music during FOCUS phases
        if (isMusicPlaying && currentPhase == PomodoroPhase.FOCUS) {
            if (!musicPlayer.isPlaying) {
                musicPlayer.start()
            }
        } else {
            if (musicPlayer.isPlaying) {
                musicPlayer.pause()
            }
        }
    }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        // --- Top Bar with Title and Sound Controls ---
        Box(modifier = Modifier.fillMaxWidth()) {
            Row(
                modifier = Modifier.align(Alignment.CenterEnd),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = { isAlarmSoundOn = !isAlarmSoundOn }) {
                    Icon(
                        imageVector = if (isAlarmSoundOn) Icons.Rounded.VolumeUp else Icons.Rounded.VolumeOff,
                        contentDescription = "Toggle Alarm Sound"
                    )
                }
                IconButton(onClick = {
                    isMusicPlaying = !isMusicPlaying
                }) {
                    Icon(
                        imageVector = if (isMusicPlaying) Icons.Rounded.MusicNote else Icons.Rounded.MusicOff,
                        contentDescription = "Toggle Background Music"
                    )
                }
            }
            Text(
                text = title,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.align(Alignment.Center)
            )
            if (isFromTask) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier.align(Alignment.CenterStart)
                ) {
                    Icon(
                        Icons.AutoMirrored.Rounded.ArrowBackIos,
                        contentDescription = "navigate back to planning screen"
                    )
                }
            }
        }

        if(isFromTask) {
            Text(text = details, fontSize = 20.sp)
        }


        // --- Timer Display ---
        Box(
            modifier = Modifier
                .fillMaxWidth(0.85f)
                .aspectRatio(1f),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator(
                progress = { progress.value },
                modifier = Modifier.fillMaxSize(),
                color = if (currentPhase == PomodoroPhase.FOCUS) red else cyan,
                strokeWidth = 14.dp,
                trackColor = Color.LightGray.copy(alpha = 0.4f),
                strokeCap = StrokeCap.Square
            )
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = currentPhase.title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    text = remainingTime.formatTime().toPersianDigits(),
                    fontSize = 72.sp,
                    fontWeight = FontWeight.Bold,
                    color = mainwhite
                )
                Text(
                    text = "دورهای انجام شده: ${pomodoroCount.toString().toPersianDigits()}",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                )
            }
        }

        // --- Control Buttons ---
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (currentPhase == PomodoroPhase.IDLE) {
                        currentPhase = PomodoroPhase.FOCUS
                    }
                    isRunning = !isRunning
                },
                shape = RoundedCornerShape(15.dp),
                modifier = Modifier.weight(1f),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRunning) secondary else red,
                    contentColor = mainwhite
                )
            ) {
                Text(text = if (isRunning) "توقف" else "شروع", fontSize = 20.sp)
            }
            TextButton(
                onClick = {
                    isRunning = false
                    currentPhase = PomodoroPhase.IDLE
                    pomodoroCount = 0
                    remainingTime = PomodoroPhase.FOCUS.durationInSeconds
                    scope.launch {
                        progress.snapTo(1f)
                    }
                },
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = "ریست",
                    fontSize = 18.sp,
                    color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.6f)
                )
            }
        }
    }
}