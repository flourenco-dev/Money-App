package com.example.minimoney.core.network.model

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class PaymentRequestModel(
    @field:Json(name = "Amount") val amount: Double,
    @field:Json(name = "InvestorProductId") val investorProductId: Int
)