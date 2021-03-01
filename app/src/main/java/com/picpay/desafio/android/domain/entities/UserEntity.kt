package com.picpay.desafio.android.domain.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_table")
data class UserEntity(
    @PrimaryKey @ColumnInfo(name="id") val id: Int,
    @ColumnInfo(name="name") val name: String,
    @ColumnInfo(name="username") val username: String,
    @ColumnInfo(name="img") val img: String
)
