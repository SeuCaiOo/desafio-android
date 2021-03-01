package com.picpay.desafio.android.data.sources.local

import com.picpay.desafio.android.data.mapper.UserMapper
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.sources.UserDataSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class UsersLocalDataSource(
    private val userDao: UserDao,
    private val ioDispatcher: CoroutineDispatcher = Dispatchers.IO
) : UserDataSource {

    override suspend fun fetchUsers(): List<User> = withContext(ioDispatcher) {
        try {
            UserMapper.fromEntityToModelList(userDao.getUsers())
        } catch (e: Exception) {
            emptyList<User>()
        }
    }

    override suspend fun saveUsers(users: List<User>) = withContext(ioDispatcher) {
        val usersDb = UserMapper.fromModelToEntityList(users)
        userDao.insertUsers(usersDb)
    }

}