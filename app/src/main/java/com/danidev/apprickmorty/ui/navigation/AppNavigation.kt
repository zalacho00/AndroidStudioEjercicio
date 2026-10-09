package com.danidev.apprickmorty.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.danidev.apprickmorty.data.model.Origin
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.ui.auth.AuthViewModel
import com.danidev.apprickmorty.ui.screens.CardDetailScreen
import com.danidev.apprickmorty.ui.screens.HomeScreen
import com.danidev.apprickmorty.ui.screens.LoginScreen
import com.danidev.apprickmorty.ui.screens.PacksScreen
import com.danidev.apprickmorty.ui.screens.ProfileScreen
import com.danidev.apprickmorty.ui.screens.SplashAppScreen
import com.danidev.apprickmorty.ui.viewModel.CharacterViewModel
import java.net.URLDecoder
import java.net.URLEncoder
import java.nio.charset.StandardCharsets

sealed class Screen(val route: String) {
    object Splash : Screen("splash_screen")
    object Login : Screen("login_screen")
    object Home : Screen("home_screen")
    object Packs : Screen("packs_screen")
    object Profile : Screen("profile_screen")
    object Detail : Screen("detail_screen/{characterName}/{characterImage}/{characterSpecies}/{characterStatus}") {
        fun createRoute(character: RickCharacter): String {
            val encodedName = URLEncoder.encode(character.name, StandardCharsets.UTF_8.toString())
            val encodedImage = URLEncoder.encode(character.image, StandardCharsets.UTF_8.toString())
            val encodedSpecies = URLEncoder.encode(character.species, StandardCharsets.UTF_8.toString())
            val encodedStatus = URLEncoder.encode(character.status, StandardCharsets.UTF_8.toString())
            return "detail_screen/$encodedName/$encodedImage/$encodedSpecies/$encodedStatus"
        }
    }
}

@Composable
fun AppNavigation(characters: List<RickCharacter>) {
    val navController = rememberNavController()
    val authViewModel: AuthViewModel = viewModel()
    val characterViewModel: CharacterViewModel = viewModel()

    LaunchedEffect(characters) {
        characterViewModel.initCharacters(characters)
    }

    val currentCharacters = if (characterViewModel.allCharacters.isNotEmpty()) {
        characterViewModel.allCharacters
    } else {
        characters
    }

    NavHost(
        navController = navController,
        startDestination = Screen.Splash.route
    ) {
        // 1. Splash
        composable(Screen.Splash.route) {
            SplashAppScreen(
                onStartClick = {
                    val destino = if (authViewModel.isLoggedIn) Screen.Home.route else Screen.Login.route
                    navController.navigate(destino) {
                        popUpTo(Screen.Splash.route) { inclusive = true }
                    }
                }
            )
        }

        // 2. Login / Registro
        composable(Screen.Login.route) {
            LoginScreen(
                viewModel = authViewModel,
                onLoggedIn = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // 3. Home
        composable(Screen.Home.route) {
            HomeScreen(
                characters = currentCharacters,
                onCharacterClick = { character ->
                    navController.navigate(Screen.Detail.createRoute(character))
                },
                onNavigateToCartas = {
                    navController.navigate(Screen.Packs.route) { launchSingleTop = true }
                },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Profile.route) { launchSingleTop = true }
                }
            )
        }

        // 4. Packs
        composable(Screen.Packs.route) {
            PacksScreen(
                characters = currentCharacters,
                onNavigateToHome = {
                    navController.navigate(Screen.Home.route) {
                        popUpTo(Screen.Home.route) { inclusive = true }
                    }
                },
                onNavigateToCartas = { },
                onNavigateToPerfil = {
                    navController.navigate(Screen.Profile.route) { launchSingleTop = true }
                }
            )
        }

        // 5. Perfil
        composable(Screen.Profile.route) {
            ProfileScreen(
                viewModel = authViewModel,
                onBack = { navController.popBackStack() },
                onLogout = {
                    authViewModel.logout()
                    navController.navigate(Screen.Login.route) {
                        popUpTo(navController.graph.id) { inclusive = true }
                    }
                }
            )
        }

        // 6. Detalle
        composable(
            route = Screen.Detail.route,
            arguments = listOf(
                navArgument("characterName") { type = NavType.StringType },
                navArgument("characterImage") { type = NavType.StringType },
                navArgument("characterSpecies") { type = NavType.StringType },
                navArgument("characterStatus") { type = NavType.StringType }
            )
        ) { backStackEntry ->
            val rawName = backStackEntry.arguments?.getString("characterName") ?: "Rick Sanchez"
            val name = try { URLDecoder.decode(rawName, StandardCharsets.UTF_8.toString()) } catch (e: Exception) { rawName }

            val rawImage = backStackEntry.arguments?.getString("characterImage") ?: ""
            val image = try { URLDecoder.decode(rawImage, StandardCharsets.UTF_8.toString()) } catch (e: Exception) { rawImage }

            val rawSpecies = backStackEntry.arguments?.getString("characterSpecies") ?: "Humano"
            val species = try { URLDecoder.decode(rawSpecies, StandardCharsets.UTF_8.toString()) } catch (e: Exception) { rawSpecies }

            val rawStatus = backStackEntry.arguments?.getString("characterStatus") ?: "Vivo"
            val status = try { URLDecoder.decode(rawStatus, StandardCharsets.UTF_8.toString()) } catch (e: Exception) { rawStatus }

            CardDetailScreen(
                character = RickCharacter(
                    id = 1,
                    name = name,
                    image = image,
                    species = species,
                    status = status,
                    origin = Origin("Tierra (C-137)")
                ),
                onBack = { navController.popBackStack() }
            )
        }
    }
}