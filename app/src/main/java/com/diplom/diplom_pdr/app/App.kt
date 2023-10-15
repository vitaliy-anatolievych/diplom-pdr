package com.diplom.diplom_pdr.app

import android.app.Application
import com.testtask.data.db.AppDataBase
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class App: Application() {

    val db by lazy { AppDataBase.getInstance(context = this) }

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@App)
            modules(listOf(core, domainModule, dataModule))
        }
    }
}