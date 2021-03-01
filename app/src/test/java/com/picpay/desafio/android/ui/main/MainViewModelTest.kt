package com.picpay.desafio.android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.picpay.desafio.android.MainCoroutineRule
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.data.repositories.FakeTestRepository
import com.picpay.desafio.android.getOrAwaitValue
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.*
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalCoroutinesApi
class MainViewModelTest {

    // Subject under test
    private lateinit var viewModel: MainViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var userRepository: FakeTestRepository

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    private val user1 = User(
        id = 1001,
        name = "Eduardo Santos",
        username = "@eduardo.santos",
        img = "https://randomuser.me/api/portraits/men/9.jpg"
    )
    private val user2 = User(
        id = 1002,
        name = "Marina Coelho",
        username = "@marina.coelho",
        img = "https://randomuser.me/api/portraits/women/37.jpg"
    )

    @Before
    fun setupViewModel() {
        userRepository = FakeTestRepository()
        userRepository.addUsers(user1, user2)
        viewModel = MainViewModel(repository = userRepository)
    }

    @Test
    fun getUsers_setUsersError() {
        userRepository.setReturnError(true)
        with(viewModel) {
            // Pause dispatcher so you can verify initial values.
            mainCoroutineRule.pauseDispatcher()
            // When fetching user
            fetchUsers()
            Assert.assertThat(progressBarVisible.getOrAwaitValue(), `is`(true))
            // Execute pending coroutines actions.
            mainCoroutineRule.resumeDispatcher()

            Assert.assertThat(hasError.getOrAwaitValue(), `is`(true))
            Assert.assertThat(progressBarVisible.getOrAwaitValue(), `is`(false))
            Assert.assertThat(recyclerViewVisible.getOrAwaitValue(), `is`(false))
            Assert.assertThat(users.getOrAwaitValue(), `is`(nullValue()))
        }

    }

    @Test
    fun getUsers_setUsersSuccess() {
        with(viewModel) {
            // Pause dispatcher so you can verify initial values.
            mainCoroutineRule.pauseDispatcher()
            // When fetching user
            fetchUsers()
            Assert.assertThat(progressBarVisible.getOrAwaitValue(), `is`(true))
            // Execute pending coroutines actions.
            mainCoroutineRule.resumeDispatcher()
            Assert.assertThat(hasError.getOrAwaitValue(), `is`(false))
            Assert.assertThat(recyclerViewVisible.getOrAwaitValue(), `is`(true))
            Assert.assertThat(users.getOrAwaitValue(), notNullValue())
        }
    }

}