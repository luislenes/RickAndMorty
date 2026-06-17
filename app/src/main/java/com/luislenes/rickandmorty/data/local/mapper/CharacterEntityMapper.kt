package com.luislenes.rickandmorty.data.local.mapper

import com.luislenes.rickandmorty.data.local.entity.CharacterEntity
import com.luislenes.rickandmorty.data.remote.dto.CharacterDto
import com.luislenes.rickandmorty.model.Character

fun CharacterEntity.toDomain(): Character = Character(
    id       = id,
    name     = name,
    status   = status,
    species  = species,
    imageUrl = imageUrl,
    gender   = gender,
    type     = type,
    origin   = origin,
    location = location
)

fun CharacterDto.toEntity(): CharacterEntity = CharacterEntity(
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

