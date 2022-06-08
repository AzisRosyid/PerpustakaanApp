package com.example.perpustakaanapp

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class ApiRetrofit {
    val apiEndPoint: ApiEndPoint
        get(){
            val network = Interceptor {s ->
                val header = s.request().newBuilder()
                    .addHeader("Authorization", Method.token.toString())
                    .build()
                val response = s.proceed(header)
                response.newBuilder().build()
            }

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            val client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .addNetworkInterceptor(network)
                .build()

            val retrofit = Retrofit.Builder()
                .baseUrl(Method.baseUrl)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            return retrofit.create(ApiEndPoint::class.java)
        }
}