package com.jop.jetpack.firebase

import android.app.Application
import com.google.firebase.FirebaseApp
import com.jop.jetpack.firebase.di.appModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.GlobalContext.startKoin

class Learning: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseApp.initializeApp(this)

        startKoin {
            androidContext(this@Learning)
            androidLogger()
            modules(appModule)
        }
    }
}