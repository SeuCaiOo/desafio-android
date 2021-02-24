package com.picpay.desafio.android.data.repositories

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.sources.remote.UsersRemoteDataSource
import com.picpay.desafio.android.domain.repositories.IUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val usersRemoteDataSource: UsersRemoteDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : IUserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return withContext(ioDispatcher) {
            try {
                val fetchUsers = usersRemoteDataSource.fetchUsers()
                return@withContext Result.Success(data = fetchUsers)
            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
    }

}