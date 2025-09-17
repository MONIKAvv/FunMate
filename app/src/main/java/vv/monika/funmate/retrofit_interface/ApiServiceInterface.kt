package vv.monika.funmate.retrofit_interface

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import vv.monika.funmate.data.QuestionsItem
import vv.monika.funmate.data.user_info
import vv.monika.funmate.model.GoogleLoginRequest
import vv.monika.funmate.model.GoogleLoginResponse

interface ApiServiceInterface {

    @Headers("Content-Type: application/json")
    @POST("google_login.php")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<GoogleLoginResponse>

}


//Interface can be single