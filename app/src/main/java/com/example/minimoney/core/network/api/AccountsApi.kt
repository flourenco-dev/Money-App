package com.example.minimoney.core.network.api

import com.example.minimoney.core.network.model.InvestorProductsModel
import com.example.minimoney.core.network.model.PaymentRequestModel
import com.example.minimoney.core.network.model.PaymentResponseModel
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AccountsApi {
    @GET("investorproducts")
    suspend fun getInvestorProducts(): InvestorProductsModel

    @POST("oneoffpayments")
    suspend fun makePayment(@Body paymentRequestModel: PaymentRequestModel): PaymentResponseModel
}