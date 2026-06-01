package com.luislenes.rickandmorty.model

data class Character(
    val id: Int,
    val name: String,
    val status: String,
    val species: String,
    val imageUrl: String,
    val gender: String,
    val type: String,
    val origin: String,
    val location: String
)
