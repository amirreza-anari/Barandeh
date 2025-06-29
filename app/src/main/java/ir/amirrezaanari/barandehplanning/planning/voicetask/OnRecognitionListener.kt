package ir.amirrezaanari.barandehplanning.planning.voicetask

interface OnRecognitionListener {
    fun onReadyForSpeech()
    fun onBeginningOfSpeech()
    fun onEndOfSpeech()
    fun onError(error: String)
    fun onResults(results: String)
}