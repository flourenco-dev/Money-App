package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginFailedModel(
    @field:Json(name = "Name") val name: String?,
    @field:Json(name = "Message") val message: String?
)