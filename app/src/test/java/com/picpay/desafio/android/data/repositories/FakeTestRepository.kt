package com.picpay.desafio.android.data.repositories

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.domain.repositories.IUserRepository
import kotlinx.coroutines.runBlocking
import java.lang.Exception
import java.util.LinkedHashMap

class FakeTestRepository : IUserRepository {

    private var usersServiceData: LinkedHashMap<Int, User> = LinkedHashMap()

    private var shouldReturnError = false

    override suspend fun getUsers(): Result<List<User>> {
        if (shouldReturnError) return Result.Error(Exception("Test Exception"))
        return Result.Success(usersServiceData.values.toList())
    }

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    fun addUsers(vararg users: User) {
        for (user in users) {
            usersServiceData[user.id] = user
        }
    }
}