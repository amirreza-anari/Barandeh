package ir.amirrezaanari.barandehplanning.api_part

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiService {
    @Headers("Content-Type: application/json")
    @POST("proxy.php")  // Update this to your actual PHP proxy path
    fun getResponse(@Body proxyRequestBody: ProxyRequestBody): Call<AIResponse>
}