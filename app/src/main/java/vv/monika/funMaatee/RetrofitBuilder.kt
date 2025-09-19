package vv.monika.funMaatee

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import vv.monika.funMaatee.retrofit_interface.ApiServiceInterface
import java.util.concurrent.TimeUnit

object RetrofitBuilder {
    private const val BASE_URL = "https://app.monikaandroidfunmate/myapi/"  // 👈 yaha sirf base url

    private val retrofit: Retrofit by lazy {
        val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val okHttpClient = OkHttpClient.Builder()
            .addInterceptor(logging)
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .writeTimeout(15, TimeUnit.SECONDS)
            .callTimeout(30, TimeUnit.SECONDS)
            .build()

        Retrofit.Builder()
            .baseUrl(BASE_URL)  // 👈domain name
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: ApiServiceInterface by lazy {
        retrofit.create(ApiServiceInterface::class.java)
    }
}

