package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponseModel(
    @field:Json(name = "User") val user: UserModel? = null,
    @field:Json(name = "Session") val session: SessionModel? = null
)