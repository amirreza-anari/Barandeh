package ir.amirrezaanari.barandehplanning.api_part

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers(
        "Content-Type: application/json",
        "Authorization: Bearer zu-d3bfc3181d7c50c6dd07f5095893b462" // کلید API خود را جایگزین کنید
    )
    @POST("chat/completions")
    fun getResponse(@Body requestBody: RequestBody): Call<AIResponse>
}