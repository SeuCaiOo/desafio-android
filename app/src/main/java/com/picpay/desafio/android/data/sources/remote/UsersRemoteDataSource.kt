package com.picpay.desafio.android.data.sources.remote

import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.sources.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersRemoteDataSource(
    private val picPayService: PicPayService,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : UserDataSource {

    override suspend fun fetchUsers(): List<User> =
        withContext(ioDispatcher) { return@withContext picPayService.getUsersSuspend() }

    override suspend fun saveUsers(users: List<User>) {
        // Not required for the remote data source
    }

}