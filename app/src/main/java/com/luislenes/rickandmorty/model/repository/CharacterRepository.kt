package com.luislenes.rickandmorty.model.repository

import androidx.paging.PagingData
import com.luislenes.rickandmorty.model.Character
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    fun getCharactersStream(): Flow<PagingData<Character>>
    suspend fun getCharacterById(id: Int): Result<Character>
}
