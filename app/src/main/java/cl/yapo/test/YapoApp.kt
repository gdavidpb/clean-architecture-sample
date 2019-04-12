package cl.yapo.test

import android.app.Application
import cl.yapo.test.di.appModule
import org.koin.android.ext.android.startKoin

open class YapoApp : Application() {
    override fun onCreate() {
        super.onCreate()

        startKoin(this, listOf(appModule))
    }
}