package ir.amirrezaanari.barandehplanning.planning.voicetask

import android.Manifest
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.ehsanmsz.mszprogressindicator.progressindicator.LineScalePulseOutProgressIndicator
import ir.amirrezaanari.barandehplanning.R
import ir.amirrezaanari.barandehplanning.planning.database.PlannerViewModel
import ir.amirrezaanari.barandehplanning.ui.theme.green
import ir.amirrezaanari.barandehplanning.ui.theme.mainwhite
import ir.amirrezaanari.barandehplanning.ui.theme.primary
import ir.amirrezaanari.barandehplanning.ui.theme.secondary

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddVoiceTaskBottomSheet(
    viewModel: PlannerViewModel,
    onDismiss: () -> Unit,
) {
    val context = LocalContext.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var isRecording by remember { mutableStateOf(false) }
    var showResultDialog by remember { mutableStateOf(false) }
    var recognizedText by remember { mutableStateOf("") }
    var errorText by remember { mutableStateOf<String?>(null) }


    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            if (isGranted) {
                isRecording = true
            } else {
                Toast.makeText(context, "برای ضبط صدا به مجوز نیاز است", Toast.LENGTH_SHORT).show()
            }
        }
    )

    val speechToTextConverter = remember {
        val listener = object : onRecognitionListener {
            override fun onReadyForSpeech() {}
            override fun onBeginningOfSpeech() {}
            override fun onEndOfSpeech() { isRecording = false }

            override fun onError(error: String) {
                if (isRecording) {
                    isRecording = false
                    errorText = "خطا: $error"
                    showResultDialog = true
                }
            }

            override fun onResults(results: String) {
                isRecording = false
                errorText = null
                recognizedText = results
                showResultDialog = true
            }
        }
        SpeechToTextConverter(context, listener)
    }

    LaunchedEffect(isRecording) {
        if (isRecording) {
            speechToTextConverter.startListening("fa-IR")
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            speechToTextConverter.destroy()
        }
    }

    if (showResultDialog) {
        VoiceResultCheckDialog(
            resultText = if (errorText != null) errorText!! else recognizedText,
            isError = errorText != null,
            onConfirm = { result ->
                // *** این بخش کلیدی‌ترین تغییر است ***
                // ۱. فراخوانی ViewModel برای پردازش متن
                viewModel.processVoiceCommand(result)
                // ۲. بستن BottomSheet
                onDismiss()
            },
            onRetry = {
                showResultDialog = false
                errorText = null
                recognizedText = ""
            }
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        shape = RoundedCornerShape(topStart = 25.dp, topEnd = 25.dp),
        containerColor = secondary,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "افزودن برنامه گفتاری",
                fontSize = 22.sp,
                fontWeight = FontWeight.Black,
                modifier = Modifier.padding(bottom = 10.dp)
            )
            Text(
                text = "دکمه ضبط رو بزن و برنامه کل روزتو برام بگو!",
                textAlign = TextAlign.Center,
            )
            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {
                    if (isRecording) {
                        isRecording = false
                        speechToTextConverter.stopListening()
                    } else {
                        permissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                    }
                },
                shape = CircleShape,
                modifier = Modifier.size(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isRecording) green else mainwhite)
            ) {
                if (!isRecording) {
                    Icon(
                        painter = painterResource(R.drawable.mic),
                        contentDescription = "mic Icon",
                        modifier = Modifier.size(40.dp),
                        tint = Color.Black
                    )
                } else {
                    BoxWithConstraints(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        val indicatorHeight = maxHeight / 1.5f
                        val indicatorLineWidth = maxHeight / 15
                        LineScalePulseOutProgressIndicator(
                            color = Color.Black,
                            lineHeightMax = indicatorHeight,
                            lineHeightMin = indicatorHeight / 2,
                            lineWidth = indicatorLineWidth,
                            lineSpacing = indicatorLineWidth
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(30.dp))
            Text(
                text = """
                    مثال: خرید از ساعت ۱۶ تا ۱۷ و درس خوندن از ساعت ۲۰ تا ۲۲
                """.trimIndent(),
                textAlign = TextAlign.Center,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}

@Composable
fun VoiceResultCheckDialog(
    resultText: String,
    isError: Boolean,
    onConfirm: (String) -> Unit,
    onRetry: () -> Unit
) {
    Dialog(onDismissRequest = onRetry) {
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = primary)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if(isError) "خطایی رخ داد" else "این نتیجه ضبطت هست. خوبه؟",
                    fontWeight = FontWeight.Bold, fontSize = 20.sp
                )
                Spacer(modifier = Modifier.height(16.dp))
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(containerColor = secondary)
                ) {
                    Text(
                        text = resultText,
                        modifier = Modifier.padding(16.dp),
                        textAlign = TextAlign.Center
                    )
                }
                Spacer(modifier = Modifier.height(24.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    if (!isError) {
                        Button(
                            onClick = { onConfirm(resultText) }, // ارسال متن نتیجه در زمان تایید
                            modifier = Modifier.weight(1f),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = green)
                        ) {
                            Text("خوبه!", color = Color.White)
                        }
                    }
                    Button(
                        onClick = onRetry,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = mainwhite)
                    ) {
                        Text("تلاش مجدد", color = Color.Black)
                    }
                }
            }
        }
    }
}