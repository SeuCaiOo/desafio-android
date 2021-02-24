package com.picpay.desafio.android.domain.repositories

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User

interface IUserRepository {
    suspend fun getUsers(): Result<List<User>>
}