package ir.amirrezaanari.barandehplanning.api_part


data class Message(
    val role: String,
    val content: String
)

data class RequestBody(
    val model: String,
    val messages: List<Message>,
    val temperature: Double = 0.7,
    val max_tokens: Int = 500
)

data class ResponseMessage(
    val role: String?,
    val content: String?
)

data class Choice(
    val message: ResponseMessage,
    val finish_reason: String?
)

data class AIResponse(
    val id: String?,
    val choices: List<Choice>,
    val usage: Usage?
)

data class Usage(
    val prompt_tokens: Int?,
    val completion_tokens: Int?,
    val total_tokens: Int?
)