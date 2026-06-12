package com.luislenes.rickandmorty.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.luislenes.rickandmorty.data.remote.api.RickAndMortyApi
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.model.Character

class CharacterPagingSource(
    private val api: RickAndMortyApi
) : PagingSource<Int, Character>() {

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Character> {
        val page = params.key ?: STARTING_PAGE
        return runCatching { api.getCharacters(page) }
            .fold(
                onSuccess = { response ->
                    LoadResult.Page(
                        data    = response.results.map { it.toDomain() },
                        prevKey = if (page == STARTING_PAGE) null else page - 1,
                        nextKey = if (response.info.next == null) null else page + 1
                    )
                },
                onFailure = { LoadResult.Error(it) }
            )
    }

    override fun getRefreshKey(state: PagingState<Int, Character>): Int? =
        state.anchorPosition?.let { state.closestPageToPosition(it)?.prevKey?.plus(1) }

    private fun CharacterDto.toDomain() = Character(
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

    companion object {
        private const val STARTING_PAGE = 1
    }
}

