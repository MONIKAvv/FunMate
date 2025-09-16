package vv.monika.funmate

import com.google.firebase.appdistribution.gradle.ApiService
import com.google.gson.Gson
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

//object RetrofitBuilder {
////    const val API = "https://backend-node-js-1-mkfw.onrender.com/"
//    const val API = "http://localhost/myapi/test.php/"
//
//    fun getInstance(): Retrofit {
//        // Add logging interceptor to see what's happening
//        val logging = HttpLoggingInterceptor().apply {
//            level = HttpLoggingInterceptor.Level.BODY
//        }
//
//        // Add timeout configuration
//        val okHttpClient = OkHttpClient.Builder()
//            .addInterceptor(logging)
//            .connectTimeout(15, TimeUnit.SECONDS) // Connection timeout
//            .readTimeout(15, TimeUnit.SECONDS)    // Read timeout
//            .writeTimeout(15, TimeUnit.SECONDS)   // Write timeout
//            .callTimeout(30, TimeUnit.SECONDS)    // Overall call timeout
//            .build()
//
//
//
//
//        return Retrofit.Builder()
//            .baseUrl(API)
//            .client(okHttpClient)
//            .addConverterFactory(GsonConverterFactory.create())
//            .build()
//    }
//}


object RetrofitBuilder {
    private const val API = "http://192.168.1.44/myapi/"
//    need server damm
    // instead of localhost (for Android emulator)

    private val retrofit by lazy {
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
            .baseUrl(API)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(Gson()))
            .build()
    }

    val api: ApiService by lazy {
        retrofit.create(ApiService::class.java)
    }
}
