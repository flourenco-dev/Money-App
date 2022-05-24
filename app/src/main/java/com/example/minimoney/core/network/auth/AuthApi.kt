package com.example.minimoney.core.network.auth

import com.example.minimoney.core.network.model.LoginRequestModel
import com.example.minimoney.core.network.model.LoginResponseModel
import retrofit2.http.Body
import retrofit2.http.POST

interface AuthApi {
    @POST("users/login")
    suspend fun login(@Body loginRequestModel: LoginRequestModel): LoginResponseModel
}