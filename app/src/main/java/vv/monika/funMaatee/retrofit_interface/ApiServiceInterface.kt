package vv.monika.funMaatee.retrofit_interface

import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.Headers
import retrofit2.http.POST

interface ApiServiceInterface {

//    @Headers("Content-Type: application/json")
@FormUrlEncoded
    @POST("google_login.php")
 fun googleLogin(
        @Field("uid") uid: String,
        @Field("name") name: String?,
        @Field("email") email: String?,
        @Field("device_id") deviceId: String
    ): Call<String>

}


//Interface can be single