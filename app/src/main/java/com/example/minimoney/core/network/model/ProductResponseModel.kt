package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class ProductResponseModel(
    @field:Json(name = "Id") val id: Int?,
    @field:Json(name = "PlanValue") val planValue: Double?,
    @field:Json(name = "Moneybox") val moneybox: Double?,
    @field:Json(name = "Product") val product: ProductModel?,

)