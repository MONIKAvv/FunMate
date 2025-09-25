package vv.monika.funMaatee.retrofit_interface

import okhttp3.RequestBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import vv.monika.funMaatee.data.ApiResponse

interface ApiServiceInterface {
    @Headers("Content-Type: application/json")
    @POST("save_user_data.php")
    fun SaveUserData(@Body body: RequestBody): Call<ResponseBody>

    @GET("fetch_promocode.php")
    fun getPromocode(): Call<ApiResponse>
}



//Interface can be single