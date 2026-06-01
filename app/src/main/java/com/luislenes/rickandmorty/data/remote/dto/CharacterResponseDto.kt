package com.luislenes.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("results") val results: List<CharacterDto>
)
