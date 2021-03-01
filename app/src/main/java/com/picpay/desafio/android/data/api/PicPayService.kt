package com.picpay.desafio.android.data.api

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import retrofit2.Call
import retrofit2.http.GET


interface PicPayService {

    @GET("users")
    fun getUsers(): Call<List<User>>

    @GET("users")
    suspend fun getUsersSuspend(): List<User>

    @GET("users")
    suspend fun getUsersResult(): Result<List<User>>

    companion object PicPayApi {
        const val url = "http://careers.picpay.com/tests/mobdev/"
    }


}