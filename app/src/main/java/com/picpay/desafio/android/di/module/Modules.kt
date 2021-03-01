package com.picpay.desafio.android.di.module

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.picpay.desafio.android.data.api.PicPayService
import com.picpay.desafio.android.data.repositories.UserRepository
import com.picpay.desafio.android.data.sources.local.UserDao
import com.picpay.desafio.android.data.sources.local.UserDatabase
import com.picpay.desafio.android.data.sources.local.UsersLocalDataSource
import com.picpay.desafio.android.data.sources.remote.UsersRemoteDataSource
import com.picpay.desafio.android.domain.repositories.IUserRepository
import com.picpay.desafio.android.ui.main.MainViewModel
import okhttp3.OkHttpClient
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


val appModule = module {

    viewModel<MainViewModel> {
        MainViewModel(get<IUserRepository>())
    }

}

val repositoryModule = module {


    factory<IUserRepository> {
        UserRepository(
            usersLocalDataSource = get<UsersLocalDataSource>(),
            usersRemoteDataSource = get<UsersRemoteDataSource>()
        )
    }
}

val apiModule = module {

    single<PicPayService> {
        Retrofit.Builder()
            .baseUrl(PicPayService.url)
            .client(providesOkHttpClient())
            .addConverterFactory(GsonConverterFactory.create(providesGson()))
            .build()
            .create(PicPayService::class.java)
    }

    factory<UsersRemoteDataSource> { UsersRemoteDataSource(picPayService = get<PicPayService>()) }
}

fun providesOkHttpClient() = OkHttpClient.Builder().build()
fun providesGson(): Gson = GsonBuilder().create()

val databaseModule = module {
    single<UserDatabase> { UserDatabase.getDatabase(androidContext()) }
    single<UserDao> { get<UserDatabase>().userDao() }
    factory<UsersLocalDataSource> { UsersLocalDataSource(userDao = get<UserDao>()) }
}

val appComponent = listOf(databaseModule, apiModule, repositoryModule, appModule)
