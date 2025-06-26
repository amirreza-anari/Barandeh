package ir.amirrezaanari.barandehplanning.planning.voicetask

// این کلاس وضعیت عملیات پردازش صدا را مشخص می‌کند
sealed class VoiceProcessingState {
    object Idle : VoiceProcessingState() // حالت بیکار
    object Loading : VoiceProcessingState() // حالت در حال پردازش (لودینگ)
    data class Error(val message: String) : VoiceProcessingState() // حالت خطا به همراه پیام
}