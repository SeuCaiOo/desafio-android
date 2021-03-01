package com.picpay.desafio.android.data.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.picpay.desafio.android.data.model.User
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.runner.RunWith


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@MediumTest
class UsersLocalDataSourceTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var localDataSource: UsersLocalDataSource
    private lateinit var database: UserDatabase

    private val user = User(
        id = 1001,
        name = "Eduardo Santos",
        username = "@eduardo.santos",
        img = "https://randomuser.me/api/portraits/men/9.jpg"
    )

    private val users = listOf(
        user, User(
            id = 1002,
            name = "Marina Coelho",
            username = "@marina.coelho",
            img = "https://randomuser.me/api/portraits/women/37.jpg"
        ), User(
            id = 1003,
            name = "Márcia da Silva",
            username = "@marcia.silva",
            img = "https://randomuser.me/api/portraits/women/57.jpg"
        ),
        User(
            id = 1004,
            name = "Fabrício Val",
            username = "@fabricio.val",
            img = "https://randomuser.me/api/portraits/men/98.jpg"
        )
    )

    @Before
    fun setup() {
        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        localDataSource = UsersLocalDataSource(
            database.userDao(), Dispatchers.Main
        )
    }

    @After
    fun cleanUp() {
        database.close()
    }

    @Test
    fun saveUsersRetrievesUsers() = runBlocking {
        localDataSource.saveUsers(users)

        val result = localDataSource.fetchUsers()
        Assert.assertEquals(users, result)
    }


}