package com.example.minimoney.core.network.api

import com.example.minimoney.core.network.model.InvestorProductsModel
import com.example.minimoney.core.network.model.PaymentResponseModel

interface ApiHelper {
    suspend fun getInvestorProducts(): InvestorProductsModel
    suspend fun makePayment(productId: Int, amount: Double): PaymentResponseModel
}