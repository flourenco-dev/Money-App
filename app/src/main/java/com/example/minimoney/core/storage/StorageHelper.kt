package com.example.minimoney.core.storage

import androidx.lifecycle.LiveData
import com.example.minimoney.core.storage.entity.UserEntity

interface StorageHelper {
    fun storeToken(token: String)
    fun getToken(): String?
    fun deleteToken()
    fun storeUserId(userId: String)
    fun getUserId(): String?
    fun deleteUserId()
    fun storeUserInputName(inputName: String)
    fun getUserInputName(): String?
    fun deleteUserInputName()
    fun getUserLiveData(): LiveData<UserEntity?>
    suspend fun getUser(): UserEntity?
    suspend fun storeUser(userEntity: UserEntity)
    suspend fun deleteUser()
}