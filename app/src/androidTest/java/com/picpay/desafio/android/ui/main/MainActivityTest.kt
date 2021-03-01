package com.picpay.desafio.android.ui.main

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Lifecycle
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.core.app.launchActivity
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.platform.app.InstrumentationRegistry
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.R
import com.picpay.desafio.android.RecyclerViewMatchers.checkRecyclerViewItem
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repositories.UserRepository
import com.picpay.desafio.android.data.sources.local.UserDatabase
import com.picpay.desafio.android.data.sources.local.UsersLocalDataSource
import com.picpay.desafio.android.data.sources.remote.UsersRemoteDataSource
import com.picpay.desafio.android.domain.repositories.IUserRepository
import kotlinx.coroutines.Dispatchers
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.context.loadKoinModules
import org.koin.core.context.unloadKoinModules
import org.koin.core.module.Module
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class MainActivityTest {

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    private lateinit var remoteDataSource: UsersRemoteDataSource
    private lateinit var service: PicPayService
    private lateinit var localDataSource: UsersLocalDataSource
    private lateinit var database: UserDatabase
    private lateinit var mockModule: Module

    private val server = MockWebServer()

    private val context = InstrumentationRegistry.getInstrumentation().targetContext


    @Before
    fun setup() {
        service = Retrofit.Builder()
            .baseUrl(server.url("/"))
            .client(OkHttpClient.Builder().build())
            .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
            .build()
            .create(PicPayService::class.java)

        remoteDataSource = UsersRemoteDataSource(picPayService = service)

        // Using an in-memory database for testing, because it doesn't survive killing the process.
        database = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            UserDatabase::class.java
        ).allowMainThreadQueries()
            .build()

        localDataSource = UsersLocalDataSource(database.userDao(), Dispatchers.Main)

        // declare module and sub-modules
        mockModule = module(override = true) {
            single<IUserRepository> {
                UserRepository(
                    usersLocalDataSource = localDataSource,
                    usersRemoteDataSource = remoteDataSource
                )
            }
            viewModel<MainViewModel> { MainViewModel(get<IUserRepository>()) }
        }

        loadKoinModules(mockModule)
    }

    @After
    fun after() {
        server.shutdown()
        unloadKoinModules(mockModule)
    }

    @Test
    fun shouldDisplayTitle() {
        launchActivity<MainActivity>().apply {
            val expectedTitle = context.getString(R.string.title)

            moveToState(Lifecycle.State.RESUMED)

            onView(withText(expectedTitle)).check(matches(isDisplayed()))
        }
    }

    @Test
    fun shouldDisplayListItem() {
        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/users" -> successResponse
                    else -> errorResponse
                }
            }
        }

//        server.start(serverPort)

        launchActivity<MainActivity>().apply {
            onView(withId(R.id.recyclerView)).check(matches(isDisplayed()))
            checkRecyclerViewItem(R.id.recyclerView, 0, withText("Eduardo Santos"))
        }

        server.close()
    }

    companion object {
        private const val serverPort = 8080

        private val successResponse by lazy {
            val body =
                "[{\"id\":1001,\"name\":\"Eduardo Santos\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@eduardo.santos\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse by lazy { MockResponse().setResponseCode(404) }
    }
}