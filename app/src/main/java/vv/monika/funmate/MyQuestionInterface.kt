package vv.monika.funmate

import retrofit2.Response
import retrofit2.http.GET


interface MyQuestionInterface {
    @GET("/questions/Maths")
    suspend fun getMathsQuestion(): Response<List<QuestionsItem>>  // Changed this!

    @GET("/questions/Alphabets")
    suspend fun getAlphabetQuestion(): Response<List<QuestionsItem>>

    @GET("/questions/Bigsmall")
    suspend fun getBigSmallQuestion(): Response<List<QuestionsItem>>

    @GET("/questions/Sound")
    suspend fun getSoundQuestion(): Response<List<QuestionsItem>>

    @GET("/questions/{Subjects}")
    suspend fun getQuestionsBySubject(
        @retrofit2.http.Path("Subjects") subject: String
    ): Response<List<QuestionsItem>>
}