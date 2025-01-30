package ir.amirrezaanari.barandehplanning.ai.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class ChatViewModel : ViewModel() {

    private val okHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://anari.freehost.io") // آدرس سرور واسط
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    private val chatProxyService = retrofit.create(ChatProxyService::class.java)


    private fun getFriendlyErrorMessage(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException, is java.net.ConnectException -> "اوه! مثل اینکه اینترنتت قطعه. 📵 یه چک بکن شاید وصل نیستی!"
            is java.net.SocketTimeoutException -> "این چه سرعتیه؟ 🐢 سرور جوابمون رو نداد. یه بار دیگه امتحان کن!"
            is retrofit2.HttpException -> when (e.code()) {
                404 -> "این چی بود کلیک کردی؟ 🕵️‍♂️ صفحه‌ای که می‌خوای وجود نداره!"
                500 -> "اوه! سرور یه مشکلی داره. 🛠️ یه وقت دیگه سر بزن!"
                else -> "یه مشکلی پیش اومده. 🌐 کد خطاش: ${e.code()}"
            }
            else -> "اوه! یه چیزی اشتباه شد. 😅 یه بار دیگه امتحان کن یا بهمون خبر بده!"
        }
    }

    fun sendMessage(userMessage: String) {

        // افزودن پیام کاربر به لیست پیام‌ها

        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage,
                participant = Participant.USER,
                isPending = true
            )
        )

        viewModelScope.launch {
            try {
                // آماده‌سازی تاریخچه چت
                val chatHistory = _uiState.value.messages.map { message ->
                    ChatHistoryItem(
                        text = message.text,
                        participant = message.participant.name
                    )
                }

                // ارسال پیام و تاریخچه چت به سرور واسط
                val response = chatProxyService.sendMessage(
                    ChatRequest(
                        message = userMessage,
                        history = chatHistory
                    )
                )

                // به‌روزرسانی آخرین پیام در حال پردازش
                _uiState.value.replaceLastPendingMessage()

                // افزودن پاسخ مدل به لیست پیام‌ها
                _uiState.value.addMessage(
                    ChatMessage(
                        text = response.response,
                        participant = Participant.MODEL,
                        isPending = false
                    )
                )
            } catch (e: Exception) {
                // در صورت خطا، پیام خطا را به لیست پیام‌ها اضافه کنید
                _uiState.value.replaceLastPendingMessage()

                val errorMessage = getFriendlyErrorMessage(e)

                _uiState.value.addMessage(
                    ChatMessage(
                        text = errorMessage,
                        participant = Participant.ERROR
                    )
                )
            }
        }
    }
}
