package com.picpay.desafio.android.data.sources

import com.picpay.desafio.android.data.model.User


interface UserDataSource {

   suspend fun fetchUsers() : List<User>

   suspend fun saveUsers(users: List<User>)

}