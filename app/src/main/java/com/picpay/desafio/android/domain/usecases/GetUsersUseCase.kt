package com.picpay.desafio.android.domain.usecases

import com.picpay.desafio.android.domain.repositories.IUserRepository

class GetUsersUseCase (
    private val repository: IUserRepository
        ) {

   suspend  operator fun invoke() = repository.getUsers()


}