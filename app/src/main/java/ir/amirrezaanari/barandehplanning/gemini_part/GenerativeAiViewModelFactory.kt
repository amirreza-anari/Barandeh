package ir.amirrezaanari.barandehplanning.gemini_part
//
//
//import androidx.compose.material3.Text
//import androidx.lifecycle.ViewModel
//import androidx.lifecycle.ViewModelProvider
//import androidx.lifecycle.viewmodel.CreationExtras
//import com.google.ai.client.generativeai.BuildConfig
//import com.google.ai.client.generativeai.GenerativeModel
//import com.google.ai.client.generativeai.type.content
//import com.google.ai.client.generativeai.type.generationConfig
//
//val GenerativeViewModelFactory = object : ViewModelProvider.Factory {
//    override fun <T : ViewModel> create(
//        modelClass: Class<T>,
//        extras: CreationExtras
//    ): T {
//        val config = generationConfig {
//            temperature = 0.7f
//        }
//
//        return with(modelClass) {
//            when {
////                isAssignableFrom(SummarizeViewModel::class.java) -> {
////                    // Initialize a GenerativeModel with the `gemini-flash` AI model
////                    // for text generation
////                    val generativeModel = GenerativeModel(
////                        modelName = "gemini-1.5-flash-latest",
////                        apiKey = BuildConfig.apiKey,
////                        generationConfig = config
////                    )
////                    SummarizeViewModel(generativeModel)
////                }
//
////                isAssignableFrom(PhotoReasoningViewModel::class.java) -> {
////                    // Initialize a GenerativeModel with the `gemini-flash` AI model
////                    // for multimodal text generation
////                    val generativeModel = GenerativeModel(
////                        modelName = "gemini-1.5-flash-latest",
////                        apiKey = BuildConfig.apiKey,
////                        generationConfig = config
////                    )
////                    PhotoReasoningViewModel(generativeModel)
////                }
//
//                isAssignableFrom(ChatViewModel::class.java) -> {
//                    // Initialize a GenerativeModel with the `gemini-flash` AI model for chat
//                    val apiKey = "AIzaSyD2t_yvE_kGeGFSCtiYoTm-oolmUkBqDKc";
//
//                    val generativeModel = GenerativeModel(
//                        modelName = "gemini-1.5-flash-latest",
//                        apiKey = apiKey,
//                        systemInstruction = content { text("تو یک متخصص برنامه ریزی هستی. اسم تو هوشیار هست. با لحن دوستانه و صمیمی جواب بده. در ابتدا برنامه ریخته شده و برنامه انجام شده کاربر برای تو ارسال میشه و تو باید اونو تحلیل کنی و در بهبود اون بهش کمک کنی و بعد باید به کاربر بگی که سوالاتش رو درباره برنامه خودش ازت بپرسه و تو باید و حتما با توحه و طبق برنامه ای کاربر برات فرستاده جواب بدی") },
//                        generationConfig = config
//                    )
//                    ChatViewModel()
//                }
//
//                else ->
//                    throw IllegalArgumentException("Unknown ViewModel class: ${modelClass.name}")
//            }
//        } as T
//    }
//}
