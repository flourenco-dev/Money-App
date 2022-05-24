package com.example.minimoney.core.network.auth

import com.example.minimoney.core.network.model.LoginRequestModel
import com.example.minimoney.core.network.model.LoginResponseModel

internal class AuthHelperImpl(private val authApi: AuthApi): AuthHelper {

    override suspend fun login(email: String, password: String): LoginResponseModel =
        authApi.login(
            LoginRequestModel(
                email = email,
                password = password
            )
        )
}