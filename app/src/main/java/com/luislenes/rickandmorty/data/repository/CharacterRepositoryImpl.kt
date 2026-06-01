package com.luislenes.rickandmorty.data.repository

import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi
) : CharacterRepository {

    override suspend fun getCharacters(): Result<List<Character>> = runCatching {
        api.getCharacters().results.map { it.toDomain() }
    }

    override suspend fun getCharacterById(id: Int): Result<Character> = runCatching {
        api.getCharacterById(id).toDomain()
    }
}

private fun CharacterDto.toDomain(): Character = Character(
    id       = id,
    name     = name,
    status   = status,
    species  = species,
    imageUrl = image,
    gender   = gender,
    type     = type,
    origin   = origin.name,
    location = location.name
)
