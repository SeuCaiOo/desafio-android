package com.picpay.desafio.android.data.api

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.model.User
import kotlinx.coroutines.Deferred
import okhttp3.OkHttpClient
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface PicPayService {

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users")
    suspend fun getUsersSuspend(): List<User>

    companion object PicPayApi {
        private const val url = "http://careers.picpay.com/tests/mobdev/"

        private val gson: Gson by lazy { GsonBuilder().create() }
        private val okHttp: OkHttpClient by lazy { OkHttpClient.Builder().build() }

        private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(url)
                .client(okHttp)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val service: PicPayService by lazy { retrofit.create(PicPayService::class.java) }
    }


}