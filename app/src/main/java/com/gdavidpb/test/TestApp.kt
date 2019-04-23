package com.gdavidpb.test

import android.app.Application
import com.gdavidpb.test.di.appModule
import org.koin.android.ext.android.startKoin

open class TestApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}