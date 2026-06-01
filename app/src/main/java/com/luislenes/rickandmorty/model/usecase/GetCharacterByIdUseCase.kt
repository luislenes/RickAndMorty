package com.luislenes.rickandmorty.model.usecase

import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository

class GetCharacterByIdUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(id: Int): Result<Character> = repository.getCharacterById(id)
}

