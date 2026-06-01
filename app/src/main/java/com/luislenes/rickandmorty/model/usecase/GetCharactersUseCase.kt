package com.luislenes.rickandmorty.model.usecase

import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository

class GetCharactersUseCase(
    private val repository: CharacterRepository
) {
    suspend operator fun invoke(): Result<List<Character>> = repository.getCharacters()
}
