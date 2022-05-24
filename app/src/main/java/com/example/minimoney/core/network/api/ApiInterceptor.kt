package com.example.minimoney.core.network.api

import com.example.minimoney.core.storage.StorageHelper
import com.example.minimoney.model.RequestFailedException
import com.example.minimoney.model.UnauthorizedException
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import timber.log.Timber

class ApiInterceptor(
    private val appId: String,
    private val appVersion: String,
    private val apiVersion: String,
    private val storageHelper: StorageHelper
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val signedRequest = chain.request().signRequest()
            val response = chain.proceed(signedRequest)
            return if (response.isSuccessful) {
                response
            } else {
                when (response.code) {
                    401 -> throw UnauthorizedException()
                    else -> throw RequestFailedException()
                }
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun Request.signRequest(): Request {
        val token: String? = storageHelper.getToken()
        Timber.d("BearerToken: $token")
        return newBuilder()
            .header("Content-Type", "application/json")
            .header("AppId", appId)
            .header("appVersion", appVersion)
            .header("apiVersion", apiVersion)
            .header("Authorization", "Bearer $token")
            .build()
    }
}