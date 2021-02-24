package com.picpay.desafio.android.data.sources

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User

interface UserDataSource {

   suspend fun fetchUsers() : List<User>

}