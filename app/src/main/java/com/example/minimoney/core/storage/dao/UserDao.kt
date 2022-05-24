package com.example.minimoney.core.storage.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.minimoney.core.storage.entity.UserEntity

@Dao
abstract class UserDao {

    @Query("SELECT * FROM UserEntity LIMIT 1")
    abstract suspend fun getUser(): UserEntity?

    @Query("SELECT * FROM UserEntity LIMIT 1")
    abstract fun getUserLiveData(): LiveData<UserEntity?>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun insert(user: UserEntity): Long

    @Update(onConflict = OnConflictStrategy.IGNORE)
    abstract suspend fun update(user: UserEntity): Int

    @Transaction
    open suspend fun insertOrUpdate(user: UserEntity): Boolean {
        return insert(user) > -1 || update(user) > -1
    }
}