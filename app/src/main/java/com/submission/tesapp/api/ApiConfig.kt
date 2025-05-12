package com.submission.tesapp.api


import com.submission.tesapp.BuildConfig
import com.submission.tesapp.utils.GateApi
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.*
import retrofit2.converter.gson.*


class ApiConfig {
    companion object {
        private const val BASE_URL_APRIORI = "https://idx-flaskk-99459457-367991773040.asia-southeast1.run.app/"

        fun getApiService(apiType: GateApi): ApiService {
            val baseUrl = when(apiType) {
                GateApi.API1 -> BASE_URL_APRIORI
            }
            val loggingInterceptor = if (BuildConfig.DEBUG) {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
            }else {
                HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.NONE)
            }
            val client = OkHttpClient.Builder()
                .addInterceptor(loggingInterceptor)
                .build()
            val retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()
            return retrofit.create(ApiService::class.java)
        }
    }
}