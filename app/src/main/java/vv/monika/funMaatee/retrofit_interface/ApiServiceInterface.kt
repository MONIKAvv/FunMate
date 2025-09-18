package vv.monika.funMaatee.retrofit_interface

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import vv.monika.funMaatee.model.GoogleLoginRequest
import vv.monika.funMaatee.model.GoogleLoginResponse

interface ApiServiceInterface {

    @Headers("Content-Type: application/json")
    @POST("google_login.php")
    suspend fun googleLogin(
        @Body request: GoogleLoginRequest
    ): Response<GoogleLoginResponse>

}


//Interface can be single