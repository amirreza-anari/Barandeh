package ir.amirrezaanari.barandehplanning.planning.voicetask

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import android.util.Log
import androidx.core.content.ContextCompat

class SpeechToTextConverter(private val context: Context, private val onRecognitionListener: OnRecognitionListener) {
    private val tagRecognition = "SpeechRecognitionTag"
    private val speechRecognizer: SpeechRecognizer =
        SpeechRecognizer.createSpeechRecognizer(context)

    private val recognitionListener: RecognitionListener = object : RecognitionListener {
        override fun onReadyForSpeech(params: Bundle?) {
            onRecognitionListener.onReadyForSpeech()
            Log.d(tagRecognition, "onReadyForSpeech:")
        }

        override fun onBeginningOfSpeech() {
            onRecognitionListener.onBeginningOfSpeech()
            Log.d(tagRecognition, "onBeginningOfSpeech:")
        }

        override fun onRmsChanged(rmsdB: Float) {}

        override fun onBufferReceived(buffer: ByteArray?) {}

        override fun onEndOfSpeech() {
            onRecognitionListener.onEndOfSpeech()
            Log.d(tagRecognition, "onEndOfSpeech:")
        }

        override fun onError(error: Int) {
            val errorMessage = when (error) {
                SpeechRecognizer.ERROR_AUDIO -> "مشکل در ضبط صدا"
                SpeechRecognizer.ERROR_CLIENT -> "مشکل در سمت کلاینت"
                SpeechRecognizer.ERROR_INSUFFICIENT_PERMISSIONS -> "مجوز کافی وجود ندارد"
                SpeechRecognizer.ERROR_NETWORK -> "مشکل در اتصال به شبکه"
                SpeechRecognizer.ERROR_NETWORK_TIMEOUT -> "زمان اتصال به شبکه تمام شد"
                SpeechRecognizer.ERROR_NO_MATCH -> "هیچ نتیجه‌ای یافت نشد"
                SpeechRecognizer.ERROR_RECOGNIZER_BUSY -> "سرویس شناسایی در حال استفاده است"
                SpeechRecognizer.ERROR_SERVER -> "مشکل در سمت سرور"
                SpeechRecognizer.ERROR_SPEECH_TIMEOUT -> "زمان صحبت تمام شد"
                else -> "خطای ناشناخته"
            }
            onRecognitionListener.onError(errorMessage)
            Log.e(tagRecognition, "onError: $errorMessage")
        }

        override fun onResults(results: Bundle?) {
            val resultArray = results?.getStringArrayList(SpeechRecognizer.RESULTS_RECOGNITION)
            if (!resultArray.isNullOrEmpty()) {
                onRecognitionListener.onResults(resultArray[0])
            } else {
                onRecognitionListener.onError("نتیجه‌ای یافت نشد")
                Log.e(tagRecognition, "نتیجه‌ای یافت نشد")
            }
        }

        override fun onPartialResults(partialResults: Bundle?) {}

        override fun onEvent(eventType: Int, params: Bundle?) {}
    }


    fun startListening(language: String) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO)
            != PackageManager.PERMISSION_GRANTED
        ) {
            onRecognitionListener.onError("مجوز ضبط صدا داده نشده است")
            Log.e(tagRecognition, "مجوز ضبط صدا داده نشده است")
            return
        }

        val speechRecognizerIntent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
            putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
            putExtra(RecognizerIntent.EXTRA_LANGUAGE, language)
        }
        speechRecognizer.setRecognitionListener(recognitionListener)
        speechRecognizer.startListening(speechRecognizerIntent)
    }

    fun stopListening() {
        speechRecognizer.stopListening()
    }

    fun destroy() {
        speechRecognizer.destroy()
    }
}