package com.luislenes.rickandmorty.presentation.navigation

sealed class Screen(val route: String) {

    data object CharacterList : Screen("character_list")

    object CharacterDetail : Screen("character_detail/{characterId}") {
        const val ARG_ID = "characterId"
        fun createRoute(id: Int) = "character_detail/$id"
    }
}

