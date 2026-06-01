package com.luislenes.rickandmorty.di

import com.luislenes.rickandmorty.presentation.CharacterViewModel
import com.luislenes.rickandmorty.presentation.detail.CharacterDetailViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val viewModelModule = module {
    viewModelOf(::CharacterViewModel)
    viewModelOf(::CharacterDetailViewModel)
}
