package vv.monika.funmate

import retrofit2.Response
import retrofit2.http.GET


interface MyQuestionInterface {
    @GET("/questions/Maths")
    suspend fun getQuestion(): Response<List<QuestionsItem>>  // Changed this!
}