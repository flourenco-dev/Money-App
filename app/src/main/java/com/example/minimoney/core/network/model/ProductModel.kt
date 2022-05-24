package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductModel(
    @field:Json(name = "Name") val name: String?,
    @field:Json(name = "FriendlyName") val friendlyName: String?,
    @field:Json(name = "ProductHexCode") val productHexCode: String?
)