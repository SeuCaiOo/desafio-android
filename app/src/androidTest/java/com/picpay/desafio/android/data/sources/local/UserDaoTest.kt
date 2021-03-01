package com.picpay.desafio.android.data.sources.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider.getApplicationContext
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.SmallTest
import com.picpay.desafio.android.data.mapper.UserMapper
import com.picpay.desafio.android.data.model.User
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@SmallTest
class UserDaoTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

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
    fun initDb() {
        // Using an in-memory database so that the information stored here disappears when the
        // process is killed.
        database = Room.inMemoryDatabaseBuilder(
            getApplicationContext(),
            UserDatabase::class.java
        ).build()
    }

    @After
    fun closeDb() = database.close()

    @Test
    fun insertUsersAndGet() = runBlockingTest {
        with(UserMapper) {
            database.userDao().insertUsers(fromModelToEntityList(users))

            val loaded = database.userDao().getUsers()

            Assert.assertFalse(loaded.isNullOrEmpty())
            Assert.assertTrue(loaded.isNotEmpty())
            Assert.assertEquals(users, fromEntityToModelList(loaded))
        }

    }

}