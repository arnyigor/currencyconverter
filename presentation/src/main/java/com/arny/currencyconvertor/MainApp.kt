package com.arny.currencyconvertor

import android.app.Application
import com.arny.currencyconvertor.di.DI
import com.facebook.stetho.Stetho

class MainApp : Application() {
    companion object {
        @JvmStatic
        lateinit var di: DI
    }

    override fun onCreate() {
        super.onCreate()
        di = DI(this)
        Stetho.initializeWithDefaults(this)
    }
}