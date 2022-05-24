package com.example.minimoney.core.network.api

import com.example.minimoney.core.network.model.InvestorProductsModel
import com.example.minimoney.core.network.model.PaymentRequestModel
import com.example.minimoney.core.network.model.PaymentResponseModel

internal class ApiHelperImpl(
    private val accountsApi: AccountsApi
    ): ApiHelper {

    override suspend fun getInvestorProducts(): InvestorProductsModel =
        accountsApi.getInvestorProducts()

    override suspend fun makePayment(
        productId: Int, amount: Double
    ): PaymentResponseModel {
        val paymentRequestModel = PaymentRequestModel(
            amount = amount,
            investorProductId = productId
        )
        return accountsApi.makePayment(paymentRequestModel)
    }
}