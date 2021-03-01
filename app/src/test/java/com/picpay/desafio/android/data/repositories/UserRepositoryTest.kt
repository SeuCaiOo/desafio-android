package com.picpay.desafio.android.data.repositories

import com.picpay.desafio.android.MainCoroutineRule
import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.sources.FakeDataSource
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.CoreMatchers.notNullValue
import org.hamcrest.core.IsEqual
import org.junit.Assert.assertEquals
import org.junit.Assert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class UserRepositoryTest {


    private val user1 = User(
        id = 1001,
        name = "Eduardo Santos",
        username = "@eduardo.santos",
        img = "https://randomuser.me/api/portraits/men/9.jpg"
    )
    private val user3 = User(
        id = 1003,
        name = "Márcia da Silva",
        username = "@marcia.silva",
        img = "https://randomuser.me/api/portraits/women/57.jpg"
    )
    private val user2 = User(
        id = 1002,
        name = "Marina Coelho",
        username = "@marina.coelho",
        img = "https://randomuser.me/api/portraits/women/37.jpg"
    )

    private val remoteUsers = listOf(user1, user2).sortedBy { it.id }
    private val localUsers = mutableListOf<User>()

    private lateinit var usersRemoteDataSource: FakeDataSource
    private lateinit var usersLocalDataSource: FakeDataSource

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Class under test
    private lateinit var userRepository: UserRepository

    @Before
    fun createRepository() {
        usersRemoteDataSource = FakeDataSource(remoteUsers)
        usersLocalDataSource = FakeDataSource(localUsers)
        // Get a reference to the class under test
        userRepository = UserRepository(
            // TODO Dispatchers.Unconfined should be replaced with Dispatchers.Main
            //  this requires understanding more about coroutines + testing
            //  so we will keep this as Unconfined for now.
            usersLocalDataSource = usersLocalDataSource,
            usersRemoteDataSource = usersRemoteDataSource,
            ioDispatcher = Dispatchers.Main
        )
    }

    @Test
    fun getUsers_requestsAllUsersFromRemoteDataSource() = mainCoroutineRule.runBlockingTest {
        val users = userRepository.getUsers() as Result.Success

        // Then users are loaded from the remote data source
        assertThat(users.data, notNullValue())
        assertThat(users.data, `is`(remoteUsers))
        assertEquals(users.data, remoteUsers)
        assertThat(users.data, IsEqual(remoteUsers))
    }

    @Test
    fun getUsers_requestsAllUsersFromLocaleDataSource() = runBlockingTest {
        localUsers.addAll(listOf(user3))

        val users = userRepository.getUsers() as Result.Success

        // Then users are loaded from the remote data source
        assertThat(users.data, notNullValue())
        assertThat(users.data, `is`(localUsers.toList()))
        assertEquals(users.data, localUsers.toList())
        assertThat(users.data, IsEqual(localUsers.toList()))
    }


}