package com.example.minimoney.core.network.auth

import com.example.minimoney.core.network.model.LoginFailedModel
import com.example.minimoney.model.LoginFailedException
import com.example.minimoney.model.LoginRequestState
import com.squareup.moshi.Moshi
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response
import okhttp3.ResponseBody

class AuthInterceptor(
    private val appId: String,
    private val appVersion: String,
    private val apiVersion: String
): Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        try {
            val signedRequest = chain.request().signRequest()
            val response = chain.proceed(signedRequest)
            return if (response.isSuccessful) {
                response
            } else {
                val loginFailed = response.body.toLoginFailedModel()
                val loginFailedState = when (loginFailed?.name) {
                    "Login failed" -> LoginRequestState.WrongEmailOrPassword
                    "Login locked" -> LoginRequestState.TooManyAttempts
                    else -> LoginRequestState.GenericError
                }
                throw LoginFailedException(loginFailedState)
            }
        } catch (e: Exception) {
            throw e
        }
    }

    private fun Request.signRequest(): Request {
        return newBuilder()
            .header("Content-Type", "application/json")
            .header("AppId", appId)
            .header("appVersion", appVersion)
            .header("apiVersion", apiVersion)
            .build()
    }

    private fun ResponseBody?.toLoginFailedModel(): LoginFailedModel? = this?.source()?.let {
        val moshiAdapter = Moshi.Builder().build().adapter(LoginFailedModel::class.java)
        moshiAdapter.fromJson(it)
    }
}