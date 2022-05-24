package com.example.minimoney.core.network.auth

import com.example.minimoney.core.network.model.LoginResponseModel

interface AuthHelper {
    suspend fun login(email: String, password: String): LoginResponseModel
}