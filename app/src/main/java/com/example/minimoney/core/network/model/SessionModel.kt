package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class SessionModel(
    @field:Json(name = "BearerToken") val token: String?
)
