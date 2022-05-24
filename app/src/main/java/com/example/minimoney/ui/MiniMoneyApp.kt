package com.example.minimoney.ui

import android.app.Application
import com.example.minimoney.BuildConfig
import com.example.minimoney.core.injection.coreModule
import com.example.minimoney.core.injection.networkModule
import com.example.minimoney.core.injection.storageModule
import com.example.minimoney.ui.injection.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level
import timber.log.Timber

class MiniMoneyApp: Application() {

    override fun onCreate() {
        super.onCreate()

        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }

        startKoin {
            androidLogger(Level.ERROR)
            androidContext(this@MiniMoneyApp)
            modules(
                networkModule,
                storageModule,
                coreModule,
                uiModule
            )
        }
    }
}