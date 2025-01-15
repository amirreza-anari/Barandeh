package ir.amirrezaanari.barandehplanning.gemini_part


import retrofit2.http.Body
import retrofit2.http.POST
import java.util.UUID

enum class Participant {
    USER, MODEL, ERROR
}

data class ChatMessage(
    val id: String = UUID.randomUUID().toString(),
    var text: String = "",
    val participant: Participant = Participant.USER,
    var isPending: Boolean = false
)

interface ChatProxyService {
    @POST("/proxygemini.php")
    suspend fun sendMessage(@Body request: ChatRequest): ChatResponse
}

data class ChatRequest(
    val message: String,
    val history: List<ChatHistoryItem>
)

data class ChatHistoryItem(
    val text: String,
    val participant: String
)

data class ChatResponse(val response: String)