package com.luislenes.rickandmorty.presentation.list

sealed class CharactersUiState {
    data object Loading : CharactersUiState()
    data class Error(val message: String) : CharactersUiState()
}