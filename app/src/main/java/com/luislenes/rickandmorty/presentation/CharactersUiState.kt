package com.luislenes.rickandmorty.presentation

import com.luislenes.rickandmorty.model.Character

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Success(val characters: List<Character>) : CharactersUiState()
    data class Error(val message: String) : CharactersUiState()
}
