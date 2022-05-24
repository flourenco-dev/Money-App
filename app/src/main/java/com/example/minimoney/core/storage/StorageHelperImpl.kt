package com.example.minimoney.core.storage

import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import com.example.minimoney.core.storage.entity.UserEntity
import com.example.minimoney.utils.tokenKey
import com.example.minimoney.utils.userIdKey
import com.example.minimoney.utils.userInputKey
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

internal class StorageHelperImpl(
    private val preferences: SharedPreferences,
    private val database: MiniMoneyDatabase
): StorageHelper {

    override fun storeToken(token: String) {
        preferences.edit().putString(tokenKey, token).apply()
    }

    override fun getToken(): String? =
        preferences.getString(tokenKey, null)

    override fun deleteToken() {
        preferences.edit().remove(tokenKey).apply()
    }

    override fun storeUserId(userId: String) {
        preferences.edit().putString(userIdKey, userId).apply()
    }

    override fun getUserId(): String? =
        preferences.getString(userIdKey, null)

    override fun deleteUserId() {
        preferences.edit().remove(userIdKey).apply()
    }

    override fun storeUserInputName(inputName: String) {
        preferences.edit().putString(userInputKey, inputName).apply()
    }

    override fun getUserInputName(): String? =
        preferences.getString(userInputKey, null)

    override fun deleteUserInputName() {
        preferences.edit().remove(userInputKey).apply()
    }

    override fun getUserLiveData(): LiveData<UserEntity?> = database.userDao.getUserLiveData()

    override suspend fun getUser(): UserEntity? = database.userDao.getUser()

    override suspend fun storeUser(userEntity: UserEntity) {
        database.userDao.insertOrUpdate(userEntity)
    }

    override suspend fun deleteUser() {
        withContext(Dispatchers.IO) {
            database.clearAllTables()
        }
    }
}