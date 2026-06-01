package com.luislenes.rickandmorty.data.remote.dto

import com.google.gson.annotations.SerializedName

data class CharacterDto(
    @SerializedName("id")       val id: Int,
    @SerializedName("name")     val name: String,
    @SerializedName("status")   val status: String,
    @SerializedName("species")  val species: String,
    @SerializedName("image")    val image: String,
    @SerializedName("gender")   val gender: String,
    @SerializedName("type")     val type: String,
    @SerializedName("origin")   val origin: LocationDto,
    @SerializedName("location") val location: LocationDto
)
