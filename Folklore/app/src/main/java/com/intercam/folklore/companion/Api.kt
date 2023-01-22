package com.intercam.folklore.companion

import android.util.Log
import com.intercam.folklore.BuildConfig
import com.intercam.folklore.model.vestimenta
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import com.intercam.folklore.rest.vestimentaApi
import okhttp3.logging.HttpLoggingInterceptor

class Api {

    companion object {
        private val TAG = "Api"
        val service: vestimentaApi = generateService()


        private fun generateService(): vestimentaApi {

            val logging = HttpLoggingInterceptor()
            logging.level = HttpLoggingInterceptor.Level.BODY

            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(logging)

                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .connectTimeout(20, TimeUnit.SECONDS)


            val retrofit: Retrofit = Retrofit.Builder()
                .baseUrl(BuildConfig.BASE_API_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .client(httpClient.build())
                .build()

            return retrofit.create(vestimentaApi::class.java)
        }


    }
}