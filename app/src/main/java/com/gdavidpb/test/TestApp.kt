package com.gdavidpb.test

import android.app.Application
import com.gdavidpb.test.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin

open class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidLogger()

            androidContext(this@TestApp)

            androidFileProperties()

            modules(appModule)
        }
    }
}