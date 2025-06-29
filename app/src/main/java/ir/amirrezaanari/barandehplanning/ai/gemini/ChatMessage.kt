package ir.amirrezaanari.barandehplanning.ai.gemini

import retrofit2.http.Body
import retrofit2.http.POST
import java.util.UUID

interface GeminiService {
    @POST("v1beta/models/gemini-2.5-flash:generateContent")
    suspend fun generateContent(
        @Body request: GeminiRequest
    ): GeminiResponse
}

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

enum class Participant {
    USER, MODEL, ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = false
)

data class ChatHistoryItem(
    val text: String,
    val participant: String
)
