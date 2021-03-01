package com.picpay.desafio.android.data.repositories

import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.sources.UserDataSource
import com.picpay.desafio.android.data.sources.local.UsersLocalDataSource
import com.picpay.desafio.android.data.sources.remote.UsersRemoteDataSource
import com.picpay.desafio.android.domain.repositories.IUserRepository
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UserRepository(
    private val usersLocalDataSource: UserDataSource,
    private val usersRemoteDataSource: UserDataSource,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.Default
) : IUserRepository {

    override suspend fun getUsers(): Result<List<User>> {
        return withContext(ioDispatcher) {
            try {
                usersLocalDataSource.let { local ->
                    val getUsersDb = local.fetchUsers()
                    val users = if (getUsersDb.isEmpty()) {
                        val fetchUsers = usersRemoteDataSource.fetchUsers()
                        local.saveUsers(fetchUsers)
                        fetchUsers
                    } else {
                        getUsersDb
                    }
                    return@withContext Result.Success(data = users)
                }

            } catch (e: Exception) {
                return@withContext Result.Error(e)
            }
        }
    }

}