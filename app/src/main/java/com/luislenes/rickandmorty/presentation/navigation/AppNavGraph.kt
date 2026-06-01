package com.luislenes.rickandmorty.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.luislenes.rickandmorty.presentation.detail.CharacterDetailScreen
import com.luislenes.rickandmorty.presentation.ui.CharacterListScreen
import com.luislenes.rickandmorty.presentation.ui.theme.RickAndMortyTheme

@Composable
fun AppNavGraph(navController: NavHostController = rememberNavController()) {
    RickAndMortyTheme {
        NavHost(
            navController = navController,
            startDestination = Screen.CharacterList.route
        ) {
            composable(Screen.CharacterList.route) {
                CharacterListScreen(
                    onCharacterClick = { id ->
                        navController.navigate(Screen.CharacterDetail.createRoute(id))
                    }
                )
            }
            composable(
                route = Screen.CharacterDetail.route,
                arguments = listOf(
                    navArgument(Screen.CharacterDetail.ARG_ID) { type = NavType.IntType }
                )
            ) {
                CharacterDetailScreen(
                    onBackClick = { navController.popBackStack() }
                )
            }
        }
    }
}

