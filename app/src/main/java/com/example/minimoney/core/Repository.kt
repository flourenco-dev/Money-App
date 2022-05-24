package com.example.minimoney.core

import androidx.lifecycle.LiveData
import com.example.minimoney.model.Account
import com.example.minimoney.model.FirstScreenState
import com.example.minimoney.model.User

interface Repository {
    fun getFirstScreenState(): FirstScreenState
    suspend fun performLogin(email: String, password: String, name: String?)
    fun getUserObservable(): LiveData<User?>
    suspend fun triggerGetUser()
    fun getAccountByIdObservable(accountId: Int): LiveData<Account?>
    suspend fun makePaymentToAccount(accountId: Int, value: Double)
    suspend fun performLogout()
}