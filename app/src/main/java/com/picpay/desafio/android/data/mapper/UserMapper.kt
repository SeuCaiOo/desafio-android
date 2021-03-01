package com.picpay.desafio.android.data.mapper

import com.picpay.desafio.android.data.model.User as UserModel
import com.picpay.desafio.android.domain.entities.UserEntity as UserEntity

object UserMapper {

     fun fromModelToEntity(model: UserModel?) : UserEntity{
        return model?.let {
            UserEntity(
                id = model.id,
                name = model.name,
                username = model.username,
                img = model.img
            )
        } ?: UserEntity(-1,"", "","")
    }

    fun fromModelToEntityList(model: List<UserModel>?): List<UserEntity> {
        return model?.map { fromModelToEntity(it) } ?: emptyList()
    }

     fun fromEntityToModel(entity: UserEntity?) : UserModel {
        return entity?.let {
            UserModel(
                id = entity.id,
                name = entity.name,
                username = entity.username,
                img = entity.img
            )
        } ?: UserModel("","", -1,"")
    }


    fun fromEntityToModelList(entity: List<UserEntity>?): List<UserModel> {
        return entity?.map { fromEntityToModel(it) } ?: emptyList()
    }

}