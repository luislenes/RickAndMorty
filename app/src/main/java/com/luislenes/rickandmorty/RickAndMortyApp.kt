package com.luislenes.rickandmorty

import android.app.Application
import com.luislenes.rickandmorty.di.domainModule
import com.luislenes.rickandmorty.di.networkModule
import com.luislenes.rickandmorty.di.repositoryModule
import com.luislenes.rickandmorty.di.viewModelModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import org.koin.core.logger.Level

class RickAndMortyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidLogger(Level.DEBUG)
            androidContext(this@RickAndMortyApp)
            modules(networkModule, repositoryModule, domainModule, viewModelModule)
        }
    }
}

