package com.example.minimoney.core.injection

import androidx.room.Room
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKey
import com.example.minimoney.R
import com.example.minimoney.core.Repository
import com.example.minimoney.core.RepositoryImpl
import com.example.minimoney.core.network.api.AccountsApi
import com.example.minimoney.core.network.api.ApiHelper
import com.example.minimoney.core.network.api.ApiHelperImpl
import com.example.minimoney.core.network.api.ApiInterceptor
import com.example.minimoney.core.network.auth.AuthHelper
import com.example.minimoney.core.network.auth.AuthHelperImpl
import com.example.minimoney.core.network.auth.AuthInterceptor
import com.example.minimoney.core.network.auth.AuthApi
import com.example.minimoney.core.network.utils.LoggerHttpClient
import com.example.minimoney.core.storage.MiniMoneyDatabase
import com.example.minimoney.core.storage.StorageHelper
import com.example.minimoney.core.storage.StorageHelperImpl
import com.example.minimoney.utils.encryptedSharedPreferencesFileName
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

val coreModule = module {
    single<Repository> {
        RepositoryImpl(
            authHelper = get(),
            storageHelper = get(),
            apiHelper = get()
        )
    }
}

val networkModule = module {
    single {
        AuthInterceptor(
            appId = androidContext().getString(R.string.app_id),
            appVersion = androidContext().getString(R.string.app_version),
            apiVersion = androidContext().getString(R.string.api_version)
        )
    }
    single<AuthApi> {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                LoggerHttpClient
                    .getOkHttpClient()
                    .addInterceptor(get<AuthInterceptor>())
                    .build()
            )
            .build()
            .create(AuthApi::class.java)
    }
    single {
        ApiInterceptor(
            appId = androidContext().getString(R.string.app_id),
            appVersion = androidContext().getString(R.string.app_version),
            apiVersion = androidContext().getString(R.string.api_version),
            storageHelper = get()
        )
    }
    single<AccountsApi> {
        Retrofit.Builder()
            .baseUrl(androidContext().getString(R.string.base_url))
            .addConverterFactory(MoshiConverterFactory.create())
            .client(
                LoggerHttpClient
                    .getOkHttpClient()
                    .addInterceptor(get<ApiInterceptor>())
                    .build()
            )
            .build()
            .create(AccountsApi::class.java)
    }
    single<ApiHelper> {
        ApiHelperImpl(accountsApi = get())
    }
    single<AuthHelper> {
        AuthHelperImpl(authApi = get())
    }
}

val storageModule = module {
    single {
        EncryptedSharedPreferences
            .create(
                androidContext(),
                encryptedSharedPreferencesFileName,
                MasterKey.Builder(
                    androidContext(),
                    MasterKey.DEFAULT_MASTER_KEY_ALIAS
                )
                    .setKeyScheme(MasterKey.KeyScheme.AES256_GCM)
                    .build(),
                EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
                EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
            )
    }
    single {
        Room.databaseBuilder(
            androidApplication(),
            MiniMoneyDatabase::class.java,
            "MiniMoneyboxDatabase.db"
        )
            .fallbackToDestructiveMigration()
            .build()
    }
    single<StorageHelper> {
        StorageHelperImpl(
            preferences = get(),
            database = get()
        )
    }
}