package com.luislenes.rickandmorty.presentation.list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.usecase.GetCharactersUseCase
import kotlinx.coroutines.flow.Flow

class CharacterViewModel(
    getCharactersUseCase: GetCharactersUseCase
) : ViewModel() {

    val characters: Flow<PagingData<Character>> = getCharactersUseCase()
        .cachedIn(viewModelScope)
}