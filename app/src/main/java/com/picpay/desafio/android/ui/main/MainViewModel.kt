package com.picpay.desafio.android.ui.main

import androidx.lifecycle.*
import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.domain.usecases.GetUsersUseCase
import kotlinx.coroutines.launch

class MainViewModel(
    private val useCase: GetUsersUseCase
): ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    init {
        fetchUsers()
    }

    private fun fetchUsers() = viewModelScope.launch {
        when (val result = useCase()) {
            is Result.Success<List<User>> -> {
                _users.value = result.data
                if (result.data.isNullOrEmpty()) _users.value = null
            }
            else -> {
                _users.value = null
            }
        }

    }
}