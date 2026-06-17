package com.luislenes.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterResponseDto(
    @SerializedName("info")    val info: InfoDto,
    @SerializedName("results") val results: List<CharacterDto>
)

data class InfoDto(
    @SerializedName("pages") val pages: Int,
    @SerializedName("next")  val next: String?
)

