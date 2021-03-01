package com.picpay.desafio.android.data.sources.remote

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.MediumTest
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.model.User
import kotlinx.coroutines.runBlocking
import okhttp3.OkHttpClient
import okhttp3.mockwebserver.Dispatcher
import okhttp3.mockwebserver.MockResponse
import okhttp3.mockwebserver.MockWebServer
import okhttp3.mockwebserver.RecordedRequest
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


@RunWith(AndroidJUnit4::class)
@MediumTest
class UsersRemoteDataSourceTest {

    private lateinit var remoteDataSource: UsersRemoteDataSource

    private val server = MockWebServer()

    private val api = Retrofit.Builder()
        .baseUrl(server.url("/"))
        .client(OkHttpClient.Builder().build())
        .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
        .build()
        .create(PicPayService::class.java)

    @Before
    fun setup() {
        remoteDataSource = UsersRemoteDataSource(picPayService = api)

        server.dispatcher = object : Dispatcher() {
            override fun dispatch(request: RecordedRequest): MockResponse {
                return when (request.path) {
                    "/users" -> successResponse
                    else -> errorResponse
                }
            }
        }
    }

    @After
    fun tearDown() {
        server.shutdown()
    }

    @Test
    fun shouldFetchUsersResponseSuccess() = runBlocking {
        val actual = remoteDataSource.fetchUsers()
        val expected = listOf(
            User(
                id = 1001,
                name = "Eduardo Santos",
                username = "@eduardo.santos",
                img = "https://randomuser.me/api/portraits/men/9.jpg"
            )
        )
        assertEquals(expected, actual)
    }


    companion object {
        private const val serverPort = 8080

        private val successResponse: MockResponse by lazy {
            val body =
                "[{\"id\":1001,\"name\":\"Eduardo Santos\",\"img\":\"https://randomuser.me/api/portraits/men/9.jpg\",\"username\":\"@eduardo.santos\"}]"

            MockResponse()
                .setResponseCode(200)
                .setBody(body)
        }

        private val errorResponse: MockResponse by lazy { MockResponse().setResponseCode(404) }
    }

}

