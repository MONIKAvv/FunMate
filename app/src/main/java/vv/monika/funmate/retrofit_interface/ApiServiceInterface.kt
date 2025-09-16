package vv.monika.funmate.retrofit_interface

import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import vv.monika.funmate.data.QuestionsItem
import vv.monika.funmate.data.user_info
import vv.monika.funmate.model.GoogleLoginRequest
import vv.monika.funmate.model.GoogleLoginResponse

interface ApiServiceInterface {
//    @GET("/questions/Maths")
//    suspend fun getMathsQuestion(): Response<List<QuestionsItem>>  // Changed this!
//
//    @GET("/questions/Alphabets")
//    suspend fun getAlphabetQuestion(): Response<List<QuestionsItem>>
//
//    @GET("/questions/Bigsmall")
//    suspend fun getBigSmallQuestion(): Response<List<QuestionsItem>>
//
//    @GET("/questions/Sound")
//    suspend fun getSoundQuestion(): Response<List<QuestionsItem>>

    @POST("google_login.php")
    suspend fun googleLogin(@Body request: GoogleLoginRequest): Response<GoogleLoginResponse>

//
//    @GET("/questions/{Subjects}")
//    suspend fun getQuestionsBySubject(
//        @Path("Subjects") subject: String
//    ): Response<List<QuestionsItem>>
}


//Interface can be single