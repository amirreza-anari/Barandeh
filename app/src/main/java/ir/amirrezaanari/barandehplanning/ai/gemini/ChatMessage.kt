package ir.amirrezaanari.barandehplanning.ai.gemini

import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.UUID

// Retrofit service برای ارتباط مستقیم با Gemini API
interface GeminiService {
    @POST("v1beta/models/gemini-1.5-flash-latest:generateContent")
    suspend fun generateContent(
        @Query("key") apiKey: String,
        @Body request: GeminiRequest
    ): GeminiResponse
}

// کلاس‌های داده‌ای مربوط به درخواست و پاسخ Gemini API
data class GeminiRequest(
    val contents: List<ContentItem>,
    val systemInstruction: SystemInstruction,
    val generationConfig: GenerationConfig
)

data class ContentItem(
    val role: String,
    val parts: List<TextPart>
)

data class TextPart(
    val text: String
)

data class SystemInstruction(
    val parts: List<TextPart>
)

data class GenerationConfig(
    val temperature: Double
)

data class GeminiResponse(
    val candidates: List<Candidate>?
)

data class Candidate(
    val content: CandidateContent?
)

data class CandidateContent(
    val parts: List<TextPart>?
)

// تعریف شرکت‌کنندگان در چت
enum class Participant {
    USER, MODEL, ERROR
}

// مدل پیام چت
data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = false
)

// مدل تاریخچه چت
data class ChatHistoryItem(
    val text: String,
    val participant: String
)
