package com.luislenes.rickandmorty.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.data.remote.paging.CharacterPagingSource
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow

class CharacterRepositoryImpl(
    private val api: RickAndMortyApi
) : CharacterRepository {

    override fun getCharactersStream(): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize           = PAGE_SIZE,
                enablePlaceholders = false
            ),
            pagingSourceFactory = { CharacterPagingSource(api) }
        ).flow

    override suspend fun getCharacterById(id: Int): Result<Character> = runCatching {
        api.getCharacterById(id).toDomain()
    }

    companion object {
        private const val PAGE_SIZE = 20
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
