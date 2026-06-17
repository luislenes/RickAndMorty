package com.luislenes.rickandmorty.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.luislenes.rickandmorty.data.local.dao.CharacterDao
import com.luislenes.rickandmorty.data.local.entity.CharacterEntity

@Database(
    entities = [CharacterEntity::class],
    version = 1,
    exportSchema = false
)
abstract class RickAndMortyDatabase : RoomDatabase() {
    abstract fun characterDao(): CharacterDao
}

