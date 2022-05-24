package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginRequestModel(
    @field:Json(name = "Email") val email: String?,
    @field:Json(name = "Password") val password: String?
)