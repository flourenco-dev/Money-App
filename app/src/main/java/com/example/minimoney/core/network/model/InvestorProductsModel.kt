package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class InvestorProductsModel(
    @field:Json(name = "TotalPlanValue") val totalPlanValue: Double?,
    @field:Json(name = "ProductResponses") val productResponses: List<ProductResponseModel>?
)
