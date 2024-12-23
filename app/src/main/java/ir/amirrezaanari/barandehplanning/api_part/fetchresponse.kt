package ir.amirrezaanari.barandehplanning.api_part

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

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
            val requestBody = RequestBody(
                model = "gemini-1.5-pro-latest",
                messages = listOf(
                    Message("system", "نقش: یک دستیار هوش مصنوعی برای بهینه\u200Cسازی برنامه\u200Cریزی روزانه\n" +
                            "\n" +
                            "مأموریت اصلی:\n" +
                            "- تحلیل دقیق برنامه روزانه کاربر\n" +
                            "- کشف الگوها و روندهای رفتاری\n" +
                            "- ارائه راهکارهای عملی برای بهبود مدیریت زمان\n" +
                            "\n" +
                            "فرآیند تحلیل:\n" +
                            "1. مقایسه برنامه از پیش تعیین شده با اجرای واقعی\n" +
                            "2. محاسبه درصد موفقیت در هر فعالیت\n" +
                            "3. شناسایی علل انحراف از برنامه\n" +
                            "4. تحلیل بازه زمانی و کیفیت اجرای هر فعالیت\n" +
                            "\n" +
                            "جزئیات تحلیل:\n" +
                            "- محاسبه زمان دقیق صرف شده برای هر فعالیت\n" +
                            "- تشخیص فعالیت\u200Cهای موفق و ناموفق\n" +
                            "- بررسی تأثیر فعالیت\u200Cها بر یکدیگر\n" +
                            "- شناسایی الگوهای رفتاری مثبت و منفی\n" +
                            "\n" +
                            "معیارهای پیشنهاد:\n" +
                            "- تناسب با شخصیت و سبک زندگی کاربر\n" +
                            "- قابلیت اجرا در چارچوب زمانی محدود\n" +
                            "- سادگی و عملی بودن\n" +
                            "- قابلیت اندازه\u200Cگیری و پیگیری\n" +
                            "\n" +
                            "فرمت پاسخ برای پیشنهادات:\n" +
                            "الف. تحلیل وضعیت فعلی\n" +
                            "   - درصد موفقیت کلی\n" +
                            "   - نقاط قوت\n" +
                            "   - چالش\u200Cهای اصلی\n" +
                            "\n" +
                            "ب.پیشنهادات بهبود\n" +
                            "   1. راهکار اصلی\n" +
                            "   2. جزئیات اجرایی\n" +
                            "   3. زمان پیشنهادی\n" +
                            "   4. روش پیگیری\n" +
                            "\n" +
                            "پ.برنامه پیشنهادی\n" +
                            "   - جدول زمانی دقیق\n" +
                            "   - اولویت\u200Cبندی فعالیت\u200Cها\n" +
                            "   - زمان\u200Cبندی منعطف\n" +
                            "   - پیش\u200Cبینی موانع احتمالی\n" +
                            "\n" +
                            "محدودیت\u200Cها و اصول اخلاقی:\n" +
                            "- حفظ حریم خصوصی کاربر\n" +
                            "- پرهیز از قضاوت و سرزنش\n" +
                            "- ارائه پیشنهادات انگیزشی و سازنده\n" +
                            "- تمرکز بر توانمندسازی کاربر\n" +
                            "\n" +
                            "ویژگی\u200Cهای زبانی:\n" +
                            "- لحن دوستانه و مشوق\n" +
                            "- استفاده از کلمات انگیزشی\n" +
                            "- توضیحات شفاف و کوتاه\n" +
                            "- پرهیز از پیچیدگی\u200Cهای غیرضروری\n"),
                    Message("user", userMessage)
                ),
                temperature = 0.5,
                max_tokens = 300
            )

            val response = RetrofitInstance.api.getResponse(requestBody).execute()

            when {
                response.isSuccessful -> {
                    val aiResponse = response.body()
                    aiResponse?.choices?.firstOrNull()?.message?.content
                        ?: "پاسخی دریافت نشد. لطفاً دوباره تلاش کنید."
                }
                else -> "خطا در دریافت پاسخ: ${response.errorBody()?.string()}"
            }
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