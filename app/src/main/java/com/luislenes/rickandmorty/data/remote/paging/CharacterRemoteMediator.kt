package com.luislenes.rickandmorty.data.remote.paging

import androidx.paging.ExperimentalPagingApi
import androidx.paging.LoadType
import androidx.paging.PagingState
import androidx.paging.RemoteMediator
import androidx.room.withTransaction
import com.luislenes.rickandmorty.data.local.db.RickAndMortyDatabase
import com.luislenes.rickandmorty.data.local.entity.CharacterEntity
import com.luislenes.rickandmorty.data.local.mapper.toEntity
import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi

@OptIn(ExperimentalPagingApi::class)
class CharacterRemoteMediator(
    private val api: RickAndMortyApi,
    private val database: RickAndMortyDatabase
) : RemoteMediator<Int, CharacterEntity>() {


    override suspend fun load(
        loadType: LoadType,
        state: PagingState<Int, CharacterEntity>
    ): MediatorResult {
        val page = when (loadType) {
            LoadType.REFRESH -> STARTING_PAGE
            LoadType.PREPEND -> return MediatorResult.Success(endOfPaginationReached = true)
            LoadType.APPEND -> {
                state.lastItemOrNull()
                    ?: return MediatorResult.Success(endOfPaginationReached = false)
                state.pages.size + 1
            }
        }

        return runCatching { api.getCharacters(page) }
            .fold(
                onSuccess = { response ->
                    database.withTransaction {
                        if (loadType == LoadType.REFRESH) {
                            database.characterDao().clearAll()
                        }
                        database.characterDao().insertAll(
                            response.results.map { it.toEntity() }
                        )
                    }
                    MediatorResult.Success(endOfPaginationReached = response.info.next == null)
                },
                onFailure = { MediatorResult.Error(it) }
            )
    }

    companion object {
        private const val STARTING_PAGE = 1
    }
}

