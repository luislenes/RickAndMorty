package com.luislenes.rickandmorty.model.repository

import com.luislenes.rickandmorty.model.Character

interface CharacterRepository {
    suspend fun getCharacters(): Result<List<Character>>
    suspend fun getCharacterById(id: Int): Result<Character>
}
