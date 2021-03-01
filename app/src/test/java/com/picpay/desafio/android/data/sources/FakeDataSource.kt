package com.picpay.desafio.android.data.sources

import com.picpay.desafio.android.data.model.User

class FakeDataSource(var users: List<User>? = listOf()) : UserDataSource {

    override suspend fun fetchUsers(): List<User> {
        return users?.toList() ?: emptyList()
    }


    override suspend fun saveUsers(users: List<User>) {
//        TODO("not implemented")
    }

}