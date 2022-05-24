package com.example.minimoney.core

import androidx.lifecycle.LiveData
import androidx.lifecycle.map
import com.example.minimoney.core.network.api.ApiHelper
import com.example.minimoney.core.network.auth.AuthHelper
import com.example.minimoney.core.network.model.InvestorProductsModel
import com.example.minimoney.core.network.model.ProductResponseModel
import com.example.minimoney.core.storage.StorageHelper
import com.example.minimoney.core.storage.entity.UserEntity
import com.example.minimoney.model.Account
import com.example.minimoney.model.FirstScreenState
import com.example.minimoney.model.UnauthorizedException
import com.example.minimoney.model.User
import com.example.minimoney.utils.get

internal class RepositoryImpl(
    private val authHelper: AuthHelper,
    private val storageHelper: StorageHelper,
    private val apiHelper: ApiHelper
    ): Repository {

    override fun getFirstScreenState(): FirstScreenState =
        if (storageHelper.getToken().isNullOrEmpty()) {
            FirstScreenState.Login
        } else {
            FirstScreenState.UserAccounts
        }

    override suspend fun performLogin(email: String, password: String, name: String?) {
        val loginModel = authHelper.login(email, password)
        val token = loginModel.session?.token
        val userId = loginModel.user?.id
        if (token.isNullOrEmpty() || userId.isNullOrEmpty()) {
            storageHelper.deleteToken()
            storageHelper.deleteUserId()
            throw UnauthorizedException()
        } else {
            storageHelper.storeToken(token)
            storageHelper.storeUserId(userId)
            if (name.isNullOrEmpty()) {
                storageHelper.deleteUserInputName()
            } else {
                storageHelper.storeUserInputName(name)
            }
        }
    }

    override fun getUserObservable(): LiveData<User?> = storageHelper.getUserLiveData().map {
        it?.toUser()
    }

    private fun UserEntity.toUser(): User =
        User(
            id = id,
            name = name,
            totalPlanValue = totalPlanValue,
            accounts = accounts
        )

    override suspend fun triggerGetUser() {
        val investorProductsModel = apiHelper.getInvestorProducts()
        storageHelper.storeUser(
            investorProductsModel.toUserEntity(
                userId = storageHelper.getUserId().get(),
                userInputName = storageHelper.getUserInputName()
            )
        )
    }

    private fun InvestorProductsModel.toUserEntity(
        userId: String,
        userInputName: String?
    ): UserEntity = UserEntity(
        id = userId,
        name = userInputName,
        totalPlanValue = totalPlanValue.get(),
        accounts = productResponses?.toAccounts() ?: listOf()
    )

    private fun List<ProductResponseModel>.toAccounts(): List<Account> = map {
        Account(
            id = it.id.get(),
            name = it.product?.name.get(),
            friendlyName = it.product?.friendlyName.get(),
            planValue = it.planValue.get(),
            moneybox = it.moneybox.get(),
            colorCode = it.product?.productHexCode.get()
        )
    }

    override fun getAccountByIdObservable(accountId: Int): LiveData<Account?> =
        // This will do the same as getUserObservable(), but if the accounts had it's own table and
        // API request it would just get the Account (ProductResponse) from the API and
        // insert/update it in the Accounts table
        storageHelper.getUserLiveData().map { userEntity ->
            userEntity?.accounts?.find {
                accountId == it.id
            }
        }

    override suspend fun makePaymentToAccount(accountId: Int, value: Double) {
        val paymentResponse = apiHelper.makePayment(amount = value, productId = accountId)
        val userEntity = storageHelper.getUser()
        val accounts = userEntity?.accounts?.toMutableList()
        accounts?.find { it.id == accountId }?.let { updatedAccount ->
            val updatedAccountIndex = accounts.indexOf(updatedAccount)
            accounts[updatedAccountIndex] =
                updatedAccount.copy(moneybox = paymentResponse.moneybox.get())
            storageHelper.storeUser(userEntity.copy(accounts = accounts))
        }
    }

    override suspend fun performLogout() {
        storageHelper.deleteToken()
        storageHelper.deleteUserId()
        storageHelper.deleteUserInputName()
        storageHelper.deleteUser()
    }
}