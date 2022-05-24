package com.example.minimoney.core.storage.converters

import androidx.room.TypeConverter
import com.example.minimoney.model.Account
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type

class AccountListConverter {

    @TypeConverter
    fun fromAccountList(accountList: List<Account>?): String? =
        Gson().toJson(accountList)

    @TypeConverter
    fun toAccountList(value: String?): List<Account>? {
        val listType: Type = object : TypeToken<List<Account?>?>() {}.type
        return Gson().fromJson(value, listType)
    }
}