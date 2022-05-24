package com.example.minimoney.core.storage

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.minimoney.core.storage.converters.AccountListConverter
import com.example.minimoney.core.storage.dao.UserDao
import com.example.minimoney.core.storage.entity.UserEntity

@Database(
    entities = [UserEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    AccountListConverter::class
)
abstract class MiniMoneyDatabase : RoomDatabase() {
    abstract val userDao: UserDao
}