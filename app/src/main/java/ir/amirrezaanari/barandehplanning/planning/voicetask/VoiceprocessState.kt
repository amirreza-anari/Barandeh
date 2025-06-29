package ir.amirrezaanari.barandehplanning.planning.voicetask

sealed class VoiceProcessingState {
    data object Idle : VoiceProcessingState()
    data object Loading : VoiceProcessingState()
    data class Error(val message: String) : VoiceProcessingState()
}