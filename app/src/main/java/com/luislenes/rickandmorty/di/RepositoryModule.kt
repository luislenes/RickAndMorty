package com.luislenes.rickandmorty.di

import com.luislenes.rickandmorty.data.repository.CharacterRepositoryImpl
import com.luislenes.rickandmorty.model.repository.CharacterRepository
import org.koin.dsl.module

val repositoryModule = module {
    single<CharacterRepository> { CharacterRepositoryImpl(get(), get()) }
}
