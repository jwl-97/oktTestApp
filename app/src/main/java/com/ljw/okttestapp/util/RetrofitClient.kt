package com.ljw.okttestapp

import okhttp3.OkHttpClient
import okhttp3.ResponseBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

object RetrofitClientV2 {
    private var instance: retrofit2.Retrofit? = null
    private fun getInstance(): retrofit2.Retrofit? {
        if (instance == null) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY

            instance = retrofit2.Retrofit.Builder()
                .baseUrl("http://localhost/.")
                .client(
                    OkHttpClient()
                        .newBuilder()
                        .addNetworkInterceptor(interceptor)
                        .readTimeout(30, TimeUnit.SECONDS)
                        .connectTimeout(30, TimeUnit.SECONDS)
                        .writeTimeout(30, TimeUnit.SECONDS)
                        .build()
                )
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build()
        }
        return instance
    }

    var iMyService: RetrofitServiceV2 =
        (getInstance() as Retrofit).create(RetrofitServiceV2::class.java)
}

interface RetrofitServiceV2 {
    @GET
    suspend fun getOktTokenizerText(@Url url: String?): Response<ResponseBody>
}