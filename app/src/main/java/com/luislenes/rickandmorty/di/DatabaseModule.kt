package com.luislenes.rickandmorty.di

import androidx.room.Room
import com.luislenes.rickandmorty.data.local.db.RickAndMortyDatabase
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val databaseModule = module {

    single {
        Room.databaseBuilder(
            androidContext(),
            RickAndMortyDatabase::class.java,
            "rick_and_morty.db"
        ).build()
    }

    single { get<RickAndMortyDatabase>().characterDao() }
}

