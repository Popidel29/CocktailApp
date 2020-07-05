package com.example.cocktailapp

import android.app.Application
import com.example.cocktailapp.di.component.AppComponent
import com.example.cocktailapp.di.component.DaggerAppComponent
import com.example.cocktailapp.di.module.DataModule

class MyApp : Application() {

    override fun onCreate() {
        component().inject(this)
        super.onCreate()
    }

    fun component(): AppComponent {

        return DaggerAppComponent
            .builder()
            .dataModule(DataModule(this))
            .build()
    }
}