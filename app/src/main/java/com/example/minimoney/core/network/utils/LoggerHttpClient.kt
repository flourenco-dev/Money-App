package com.example.minimoney.core.network.utils

import com.example.minimoney.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor

object LoggerHttpClient {
    fun getOkHttpClient(): OkHttpClient.Builder = OkHttpClient.Builder().also {
        if (BuildConfig.DEBUG) {
            val interceptor = HttpLoggingInterceptor()
            interceptor.setLevel(HttpLoggingInterceptor.Level.BODY)
            it.addInterceptor(interceptor)
        }
    }
}