package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danidev.apprickmorty.data.model.Origin
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.ui.screens.CardDetailScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.SplashAppScreen
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Home : Screen("home_screen")
    object Detail : Screen("detail_screen/{characterName}/{characterImage}/{characterSpecies}/{characterStatus}") {
        fun createRoute(character: RickCharacter): String {
            val encodedImage = URLEncoder.encode(character.image, StandardCharsets.UTF_8.toString())
            return "detail_screen/${character.name}/$encodedImage/${character.species}/${character.status}"
        }
    }
}

@Composable
fun AppNavigation(characters: List<RickCharacter>) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash Screen
        composable(Screen.Splash.route) {
            SplashAppScreen(
                onStartClick = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Home Screen
        composable(Screen.Home.route) {
            HomeScreen(
                characters = characters,
                onCharacterClick = { character ->
                    navController.navigate(Screen.Detail.createRoute(character))
                }
            )
        }

        // 3. Detail Screen
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("characterName") { type = NavType.StringType },
                navArgument("characterImage") { type = NavType.StringType },
                navArgument("characterSpecies") { type = NavType.StringType },
                navArgument("characterStatus") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val name = backStackEntry.arguments?.getString("characterName") ?: "Rick Sanchez"
            val rawImage = backStackEntry.arguments?.getString("characterImage") ?: ""
            val image = URLDecoder.decode(rawImage, StandardCharsets.UTF_8.toString())
            val species = backStackEntry.arguments?.getString("characterSpecies") ?: "Humano"
            val status = backStackEntry.arguments?.getString("characterStatus") ?: "Vivo"

            val selectedCharacter = RickCharacter(
                id = 1,
                name = name,
                image = image,
                species = species,
                status = status,
                origin = Origin("Tierra (C-137)")
            )

            CardDetailScreen(character = selectedCharacter)
        }
    }
}