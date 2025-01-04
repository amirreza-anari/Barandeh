package ir.amirrezaanari.barandehplanning.ui_part.plan_ui

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.util.Log
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import com.orbitalsonic.speechrecognition.SpeechToTextConverter
import ir.amirrezaanari.barandehplanning.voice_part.onRecognitionListener
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface WitApiService {
    @GET("message")
    fun getMessage(
        @Header("Authorization") authHeader: String,
        @Query("v") version: String,
        @Query("q") query: String
    ): Call<WitResponse>
}

data class WitResponse(
    val text: String?, // متنی که API تحلیل کرده است
    val entities: Map<String, List<Entity>>? // موجودیت‌ها
)

data class Entity(
    val body: String?,
    val confidence: Double?,
    val value: String?,
    val name: String?,
    val role: String?,
    val start: Int?,
    val end: Int?,
    val entities: Map<String, Any>?
)

object RetrofitInstance {
    private const val BASE_URL = "https://api.wit.ai/"

    val api: WitApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(WitApiService::class.java)
    }
}
@Composable
fun AddVoicePTask() {
    val context = LocalContext.current
    var isRecording by remember { mutableStateOf(false) }
    var recordedText by remember { mutableStateOf("اینجا متن ضبط شده نمایش داده می‌شود.") }
    var apiResponse by remember { mutableStateOf("") }  // متغیر برای ذخیره پاسخ API
    var taskName by remember { mutableStateOf("") }  // برای ذخیره taskname
    var startTime by remember { mutableStateOf("") }  // برای ذخیره start_time
    var endTime by remember { mutableStateOf("") }  // برای ذخیره end_time


    fun sendToWitApi(text: String) {
        // کد ارسال درخواست به API
        val witAuthToken = "Bearer 4DOSUT6PPPVRUY7GWLSFUVDW62QRYHZD"
        val apiService = RetrofitInstance.api
        apiService.getMessage(witAuthToken, "20250104", text)
            .enqueue(object : Callback<WitResponse> {
                override fun onResponse(call: Call<WitResponse>, response: Response<WitResponse>) {
                    if (response.isSuccessful) {
                        val witResponse = response.body()
                        // استخراج موجودیت‌ها
                        val entities = witResponse?.entities
                        Log.d("WitResponse", "Entities: $entities")

                        // استخراج مقادیر موجودیت‌ها
                        taskName = entities?.get("task_name:task_name")?.joinToString(", ") { it.value ?: "مقدار موجود نیست" } ?: "مقدار موجود نیست"
                        startTime = entities?.get("start_time:start_time")?.firstOrNull()?.value ?: "مقدار موجود نیست"
                        endTime = entities?.get("end_time:end_time")?.firstOrNull()?.value ?: "مقدار موجود نیست"

                        // چاپ مقادیر برای بررسی
                        Log.d("WitResponse", "taskName: $taskName")
                        Log.d("WitResponse", "startTime: $startTime")
                        Log.d("WitResponse", "endTime: $endTime")
                    } else {
                        apiResponse = "خطا در دریافت پاسخ"
                    }
                }

                override fun onFailure(call: Call<WitResponse>, t: Throwable) {
                    apiResponse = "مشکل در ارتباط با سرور"
                }
            })
    }



    // SpeechToTextConverter setup
    val speechToTextConverter = remember {
        SpeechToTextConverter(
            context = context,
            onRecognitionListener = object : onRecognitionListener {
                override fun onReadyForSpeech() {
//                    Toast.makeText(context, "آماده برای ضبط", Toast.LENGTH_SHORT).show()
                }

                override fun onBeginningOfSpeech() {
                    Toast.makeText(context, "شروع ضبط", Toast.LENGTH_SHORT).show()
                }

                override fun onEndOfSpeech() {
                    Toast.makeText(context, "پایان ضبط", Toast.LENGTH_SHORT).show()
                }

                override fun onError(error: String) {
                    Toast.makeText(context, "خطا: $error", Toast.LENGTH_SHORT).show()
                }

                override fun onResults(results: String) {
                    recordedText = results
                    sendToWitApi(results)  // ارسال به API پس از دریافت متن
                }
            }
        )
    }

    // Request permission launcher
    val requestPermissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            Toast.makeText(context, "مجوز ضبط صدا فعال شد.", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(context, "مجوز ضبط صدا رد شد.", Toast.LENGTH_SHORT).show()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // نمایش متن ضبط شده
        Text(recordedText)

        Spacer(modifier = Modifier.height(50.dp))

        Button(
            onClick = {
                if (ContextCompat.checkSelfPermission(
                        context,
                        Manifest.permission.RECORD_AUDIO
                    ) == PackageManager.PERMISSION_GRANTED
                ) {
                    if (!isRecording) {
                        speechToTextConverter.startListening("fa-IR") // شروع ضبط
                        isRecording = true
                    } else {
                        speechToTextConverter.stopListening() // پایان ضبط
                        isRecording = false
                    }
                } else {
                    // درخواست مجوز
                    requestPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                }
            }
        ) {
            Text(if (!isRecording) "شروع" else "پایان")
        }

        Spacer(modifier = Modifier.height(30.dp))  // فاصله میان دکمه و پاسخ API

        // نمایش مقادیر taskname, start_time, end_time
        Text("taskname value: $taskName")
        Text("start_time value: $startTime")
        Text("end_time value: $endTime")
    }
}
