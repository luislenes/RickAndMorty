package com.luislenes.rickandmorty.di

import com.luislenes.rickandmorty.model.usecase.GetCharacterByIdUseCase
import com.luislenes.rickandmorty.model.usecase.GetCharactersUseCase
import org.koin.dsl.module

val domainModule = module {
    factory { GetCharactersUseCase(get()) }
    factory { GetCharacterByIdUseCase(get()) }
}
