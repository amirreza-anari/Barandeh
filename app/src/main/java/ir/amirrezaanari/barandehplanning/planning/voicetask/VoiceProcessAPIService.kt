package ir.amirrezaanari.barandehplanning.planning.voicetask

import ir.amirrezaanari.barandehplanning.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import java.util.concurrent.TimeUnit

data class VoiceRequest(val text: String)

data class GeminiResponse(val tasks: List<TaskItem>)

data class TaskItem(
    val task_name: String,
    val start_time: String,
    val end_time: String
)

interface GeminiApiService {
    @POST(".")
    fun getTasksFromText(@Body request: VoiceRequest): Call<GeminiResponse>
}

object RetrofitInstance {
    private const val BASE_URL = BuildConfig.VOICE_TASK_BASE_URL

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