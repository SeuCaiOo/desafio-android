package com.picpay.desafio.android.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.picpay.desafio.android.data.Result
import com.picpay.desafio.android.data.model.User
import com.picpay.desafio.android.domain.repositories.IUserRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel(
    private val repository: IUserRepository
) : ViewModel() {

    private val _users = MutableLiveData<List<User>>()
    val users: LiveData<List<User>>
        get() = _users

    private val _recyclerViewVisible = MutableLiveData<Boolean>()
    val recyclerViewVisible: LiveData<Boolean> = _recyclerViewVisible

    private val _progressBarVisible = MutableLiveData<Boolean>()
    val progressBarVisible: LiveData<Boolean> = _progressBarVisible

    private val _hasError = MutableLiveData<Boolean>()
    val hasError: LiveData<Boolean> = _hasError

    init {
//        fetchUsers()
    }

    fun fetchUsers(): Job {
        _progressBarVisible.value = true
        return viewModelScope.launch {
            _users.value = when (val result = repository.getUsers()) {
                is Result.Success<List<User>> -> {
                    if (result.data.isNullOrEmpty()) null else result.data
                }
                else -> null
            }
            _progressBarVisible.value = false
            _hasError.value = users.value.isNullOrEmpty()
            _recyclerViewVisible.value = hasError.value?.not() ?: true


        }
    }
}