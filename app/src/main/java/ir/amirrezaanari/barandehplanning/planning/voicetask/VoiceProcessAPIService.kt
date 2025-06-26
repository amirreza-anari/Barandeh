package ir.amirrezaanari.barandehplanning.planning

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

// --- NEW Data Models for Gemini Response ---

// Represents the request body sent TO the worker
data class VoiceRequest(val text: String)

// Represents the JSON response FROM the worker
data class GeminiResponse(val tasks: List<TaskItem>)

data class TaskItem(
    val task_name: String,
    val start_time: String,
    val end_time: String
)

// --- NEW Retrofit Service Interface ---
interface GeminiApiService {
    @POST(".") // Send POST request to the base URL of the worker
    fun getTasksFromText(@Body request: VoiceRequest): Call<GeminiResponse>
}

// --- Updated Retrofit Instance ---
object RetrofitInstance {
    // *** این آدرس را با آدرس ورکر خود جایگزین کنید ***
    private const val BASE_URL = "https://wit-ai.0amir0kylo0.workers.dev/" // نام ورکر خود را جایگزین کنید

    val api: GeminiApiService by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)

        val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
            .create(GeminiApiService::class.java)
    }
}