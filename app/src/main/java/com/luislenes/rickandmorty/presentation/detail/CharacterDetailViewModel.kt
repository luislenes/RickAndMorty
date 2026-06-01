package com.luislenes.rickandmorty.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.luislenes.rickandmorty.model.usecase.GetCharacterByIdUseCase
import com.luislenes.rickandmorty.presentation.navigation.Screen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class CharacterDetailViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCharacterByIdUseCase: GetCharacterByIdUseCase
) : ViewModel() {

    private val characterId: Int = checkNotNull(savedStateHandle[Screen.CharacterDetail.ARG_ID])

    private val _uiState = MutableStateFlow<CharacterDetailUiState>(CharacterDetailUiState.Loading)
    val uiState: StateFlow<CharacterDetailUiState> = _uiState.asStateFlow()

    init {
        fetchCharacter()
    }

    fun fetchCharacter() {
        viewModelScope.launch {
            _uiState.value = CharacterDetailUiState.Loading
            getCharacterByIdUseCase(characterId)
                .onSuccess { _uiState.value = CharacterDetailUiState.Success(it) }
                .onFailure { _uiState.value = CharacterDetailUiState.Error(it.message.orEmpty()) }
        }
    }
}

