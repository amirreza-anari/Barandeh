package ir.amirrezaanari.barandehplanning.gemini_part

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.ai.client.generativeai.GenerativeModel
import com.google.ai.client.generativeai.type.asTextOrNull
import com.google.ai.client.generativeai.type.content
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
        .connectTimeout(30, TimeUnit.SECONDS) // زمان انتظار برای اتصال
        .readTimeout(30, TimeUnit.SECONDS)    // زمان انتظار برای خواندن پاسخ
        .writeTimeout(30, TimeUnit.SECONDS)   // زمان انتظار برای ارسال درخواست
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl("http://anari.freehost.io/") // آدرس سرور واسط
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()
    private val chatProxyService = retrofit.create(ChatProxyService::class.java)

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
                _uiState.value.addMessage(
                    ChatMessage(
                        text = e.localizedMessage ?: "An error occurred",
                        participant = Participant.ERROR
                    )
                )
            }
        }
    }
}
