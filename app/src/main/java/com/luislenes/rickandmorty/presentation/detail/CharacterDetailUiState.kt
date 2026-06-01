package com.luislenes.rickandmorty.presentation.detail

import com.luislenes.rickandmorty.model.Character

sealed class CharacterDetailUiState {
    data object Loading : CharacterDetailUiState()
    data class Success(val character: Character) : CharacterDetailUiState()
    data class Error(val message: String) : CharacterDetailUiState()
}

