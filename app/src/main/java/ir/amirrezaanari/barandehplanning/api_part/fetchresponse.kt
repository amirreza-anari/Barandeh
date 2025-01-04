package ir.amirrezaanari.barandehplanning.api_part

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

//@Composable
//fun ChatApp() {
//    var userInput by remember { mutableStateOf("") }
//    var aiResponse by remember { mutableStateOf("") }
//    val coroutineScope = rememberCoroutineScope()
//    val keyboardController = LocalSoftwareKeyboardController.current
////    var plainResponse = aiResponse.replace(Regex("\\*\\*|\\*|`|_|~|\\[.*?\\]\\(.*?\\)"), "")
//
//    CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl ) {
//        Column(
//            modifier = Modifier
//                .fillMaxSize()
//                .padding(16.dp),
//            verticalArrangement = Arrangement.spacedBy(8.dp)
//        ) {
//            BasicTextField(
//                value = userInput,
//                onValueChange = { userInput = it },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(150.dp)
//                    .border(2.dp, color = Color(0xFF2962FF))
//                    .padding(top = 8.dp, start = 8.dp),
//
//                )
//            var isLoading by remember { mutableStateOf(false) }
//
//            Row {
//                Button(
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFF2962FF)
//                    ),
//                    onClick = {
//                        keyboardController?.hide()
//                        coroutineScope.launch {
//                            isLoading = true
//                            aiResponse = fetchAIResponse(userInput)
//                            isLoading = false
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth(0.7f)
//                ) {
//                    Text("ارسال")
//                }
//                Spacer(modifier = Modifier.size(5.dp))
//                Button(
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Color(0xFFD50000)
//                    ),
//                    onClick = {
//                        coroutineScope.launch {
//                            userInput = "امروز:\n" +
//                                    "برنامه ریخته شده:\n" +
//                                    "2 تا 5 ظهر درس\n" +
//                                    "5 تا 7 تمرین زبان\n" +
//                                    "7 تا 8 ورزش\n" +
//                                    "8 تا 9 استراحت\n" +
//                                    "برنامه واقعی که عمل شده:\n" +
//                                    "4 تا 5 درس\n" +
//                                    "5 تا 8 استراحت\n" +
//                                    "ورزش انجام نشده\n" +
//                                    "8 تا 11 دوباره استراحت\n" +
//                                    "دیروز:\n" +
//                                    "برنامه ریخته شده:\n" +
//                                    "1 تا 4 درس\n" +
//                                    "4 تا 5 تمرین زبان\n" +
//                                    "7 تا 8 ورزش\n" +
//                                    "8 تا 9 استراحت\n" +
//                                    "برنامه واقعی که عمل شده:\n" +
//                                    "4 تا 5 درس\n" +
//                                    "5 تا 8 استراحت\n" +
//                                    "ورزش 8 تا 9\n" +
//                                    "9 تا 11 دوباره استراحت"
//                        }
//                    },
//                    modifier = Modifier.fillMaxWidth()
//                ) {
//                    Text("پیام")
//                }
//            }
//
//
//            Text("پاسخ:",fontWeight = FontWeight.Bold ,modifier = Modifier.padding(top = 16.dp))
//
//            HorizontalDivider(color = Color.Black)
//
//            if (isLoading) {
//                Row(
//                    modifier = Modifier.fillMaxWidth()
//                        .fillMaxSize(),
//                    verticalAlignment = Alignment.CenterVertically,
//                    horizontalArrangement = Arrangement.Center
//                ) {
//                    CircularProgressIndicator(modifier = Modifier.padding(top = 16.dp), color = Color(0xFF2962FF))
//                }
//            }
//            LazyColumn {
//                item {
//                    Text(aiResponse,modifier = Modifier.padding(8.dp))
//
//                }
//            }
//
//        }
//    }
//
//}



suspend fun fetchAIResponse(userMessage: String): String {
    return withContext(Dispatchers.IO) {
        try {
            // Create ProxyRequestBody with the user message and default values
            val proxyRequestBody = ProxyRequestBody(
                userInput = userMessage
            )

            val response = RetrofitInstance.api.getResponse(proxyRequestBody).execute()

            when {
                response.isSuccessful -> {
                    val aiResponse = response.body()
                    aiResponse?.choices?.firstOrNull()?.message?.content
                        ?: "پاسخی دریافت نشد. لطفاً دوباره تلاش کنید."
                }
                else -> "خطا در دریافت پاسخ: ${response.errorBody()?.string()}"
            }
        } catch (e: UnknownHostException) {
            "اتصال به اینترنت برقرار نیست. لطفاً اتصال اینترنت خود را بررسی کنید."
        } catch (e: SSLHandshakeException) {
            "مشکل در اتصال امن به سرور. ممکن است VPN شما فعال باشد."
        } catch (e: IOException) {
            "خطا در ارتباط با سرور. لطفاً اتصال اینترنت خود را بررسی کنید."
        } catch (e: Exception) {
            "خطای سیستمی: ${e.localizedMessage}"
        }
    }
}
//@Composable
//fun SpeechToTextScreen() {
//    val context = LocalContext.current
//    var transcribedText by remember { mutableStateOf("") }
//    var isRecording by remember { mutableStateOf(false) }
//    var voskModel by remember { mutableStateOf<Model?>(null) }
//    var recognizer by remember { mutableStateOf<Recognizer?>(null) }
//
//    // بارگذاری مدل Vosk
//    LaunchedEffect(Unit) {
//        try {
//            voskModel = Model(context.assets, "model-fa")
//        } catch (e: IOException) {
//            transcribedText = "خطا در بارگذاری مدل: ${e.message}"
//        }
//    }
//
//    // چک کردن مجوز میکروفون
//    val requestPermissionLauncher = rememberLauncherForActivityResult(
//        contract = ActivityResultContracts.RequestPermission()
//    ) { isGranted ->
//        if (!isGranted) {
//            transcribedText = "مجوز ضبط صدا داده نشد"
//        }
//    }
//
//    Column(
//        modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp),
//        horizontalAlignment = Alignment.CenterHorizontally,
//        verticalArrangement = Arrangement.Center
//    ) {
//        Button(
//            onClick = {
//                // چک کردن مجوز
//                if (ContextCompat.checkSelfPermission(
//                        context,
//                        Manifest.permission.RECORD_AUDIO
//                    ) != PackageManager.PERMISSION_GRANTED
//                ) {
//                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
//                } else {
//                    // شروع ضبط
//                    isRecording = true
//                    try {
//                        recognizer = Recognizer(voskModel, 16000f)
//                        // شروع ضبط در یک Coroutine یا Thread
//                        Thread {
//                            while (isRecording) {
//                                val result = recognizer?.partialResult
//                                if (!result.isNullOrEmpty()) {
//                                    transcribedText = result
//                                }
//                            }
//                        }.start()
//                    } catch (e: Exception) {
//                        transcribedText = "خطا در شروع ضبط: ${e.message}"
//                    }
//                }
//            },
//            enabled = voskModel != null
//        ) {
//            Text(text = if (isRecording) "در حال ضبط..." else "شروع ضبط")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Button(
//            onClick = {
//                isRecording = false
//                // توقف ضبط و دریافت متن
//                recognizer?.let {
//                    transcribedText = it.finalResult
//                    it.close()
//                }
//            },
//            enabled = isRecording
//        ) {
//            Text(text = "پایان ضبط")
//        }
//
//        Spacer(modifier = Modifier.height(16.dp))
//
//        Text(
//            text = transcribedText,
//            modifier = Modifier
//                .fillMaxWidth()
//                .padding(16.dp)
//        )
//    }
//}
//
//@Preview(showBackground = true)
//@Composable
//fun DefaultPreview() {
//    SpeechToTextScreen()
//}