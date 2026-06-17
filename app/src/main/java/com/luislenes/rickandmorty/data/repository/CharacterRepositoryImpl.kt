package com.luislenes.rickandmorty.data.repository

import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import com.luislenes.rickandmorty.data.local.db.RickAndMortyDatabase
import com.luislenes.rickandmorty.data.local.mapper.toDomain
import com.luislenes.rickandmorty.data.local.mapper.toEntity
import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.data.remote.paging.CharacterRemoteMediator
import com.luislenes.rickandmorty.model.Character
import com.luislenes.rickandmorty.model.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalPagingApi::class)
class CharacterRepositoryImpl(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabase
) : CharacterRepository {

    override fun getCharactersStream(): Flow<PagingData<Character>> =
        Pager(
            config = PagingConfig(
                pageSize           = PAGE_SIZE,
                enablePlaceholders = false
            ),
            remoteMediator      = CharacterRemoteMediator(api, database),
            pagingSourceFactory = { database.characterDao().pagingSource() }
        ).flow.map { pagingData -> pagingData.map { it.toDomain() } }

    override suspend fun getCharacterById(id: Int): Result<Character> {
        val networkResult = runCatching { api.getCharacterById(id) }

        if (networkResult.isSuccess) {
            val dto = networkResult.getOrThrow()
            database.characterDao().insertAll(listOf(dto.toEntity()))
            return Result.success(dto.toDomain())
        }

        return database.characterDao().getById(id)
            ?.let { Result.success(it.toDomain()) }
            ?: networkResult.map { it.toDomain() }
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
