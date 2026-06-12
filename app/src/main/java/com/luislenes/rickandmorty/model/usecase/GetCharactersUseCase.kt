package com.luislenes.rickandmorty.model.usecase

import androidx.paging.PagingData
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    operator fun invoke(): Flow<PagingData<Character>> = repository.getCharactersStream()
}
