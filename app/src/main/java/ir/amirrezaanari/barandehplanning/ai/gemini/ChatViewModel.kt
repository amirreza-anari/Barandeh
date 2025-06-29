package ir.amirrezaanari.barandehplanning.ai.gemini

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import ir.amirrezaanari.barandehplanning.BuildConfig
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit



class ChatViewModel : ViewModel() {

    companion object {
        private const val BASE_URL = BuildConfig.GEMINI_CHAT_BASE_URL
    }

    private val okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .build()

    private val retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val geminiService = retrofit.create(GeminiService::class.java)

    private val _uiState = MutableStateFlow(ChatUiState())
    val uiState: StateFlow<ChatUiState> = _uiState.asStateFlow()

    private val _systemPrompt = MutableStateFlow("")

    fun setSystemPrompt(prompt: String) {
        _systemPrompt.value = prompt
    }

    private fun getFriendlyErrorMessage(e: Exception): String {
        return when (e) {
            is java.net.UnknownHostException, is java.net.ConnectException -> "اوه! مثل اینکه اینترنتت قطعه. 📵 چک بکن شاید وصل نیستی!"
            is java.net.SocketTimeoutException -> "این چه سرعتیه؟ 🐢 سرور جوابمون رو نداد. یه بار دیگه امتحان کن!"
            is retrofit2.HttpException -> when (e.code()) {
                404 -> "اوه! یه چیزی اشتباه شد. 😅 یه بار دیگه امتحان کن!"
                500 -> "اوه! سرور یه مشکلی داره. 🛠️ یه وقت دیگه سر بزن!"
                else -> "یه مشکلی پیش اومده. 🌐 کد خطاش: ${e.code()}"
            }
            else -> "اوه! یه چیزی اشتباه شد. 😅 یه بار دیگه امتحان کن!"
        }
    }

    fun sendMessage(userMessage: String) {
        _uiState.value.addMessage(
            ChatMessage(
                text = userMessage,
                participant = Participant.USER,
                isPending = true
            )
        )

        viewModelScope.launch {
            try {
                val chatHistory = _uiState.value.messages.map { message ->
                    ChatHistoryItem(
                        text = message.text,
                        participant = message.participant.name
                    )
                }

                val contents = chatHistory.map { historyItem ->
                    ContentItem(
                        role = if (historyItem.participant == Participant.USER.name) "user" else "model",
                        parts = listOf(TextPart(text = historyItem.text))
                    )
                }.toMutableList()

                contents.add(
                    ContentItem(
                        role = "user",
                        parts = listOf(TextPart(text = userMessage))
                    )
                )

                val systemInstructionText = _systemPrompt.value.ifBlank {
                    "تو یک دستیار باهوش و مهربان هستی."
                }

                val geminiRequest = GeminiRequest(
                    contents = contents,
                    systemInstruction = SystemInstruction(
                        parts = listOf(TextPart(text = systemInstructionText))
                    ),
                    generationConfig = GenerationConfig(
                        temperature = 0.6
                    )
                )

                val response = geminiService.generateContent(geminiRequest)

                _uiState.value.replaceLastPendingMessage()

                val modelResponse = response.candidates?.firstOrNull()?.content?.parts?.firstOrNull()?.text
                    ?: "خطا: پاسخ معتبر دریافت نشد."

                _uiState.value.addMessage(
                    ChatMessage(
                        text = modelResponse,
                        participant = Participant.MODEL,
                        isPending = false
                    )
                )
            } catch (e: Exception) {
                e.printStackTrace()
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
