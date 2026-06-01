package com.luislenes.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class LocationDto(
    @SerializedName("name") val name: String
)

