package com.danidev.apprickmorty

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.danidev.apprickmorty.data.model.Origin
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.ui.navigation.AppNavigation
import com.danidev.apprickmorty.ui.theme.ApprickmortyTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val sampleCharacters = listOf(
            RickCharacter(
                id = 1,
                name = "Rick Sanchez",
                image = "https://rickandmortyapi.com/api/character/avatar/1.jpeg",
                species = "Humano",
                status = "Vivo",
                origin = Origin("Tierra (C-137)")
            ),
            RickCharacter(
                id = 2,
                name = "Morty Smith",
                image = "https://rickandmortyapi.com/api/character/avatar/2.jpeg",
                species = "Humano",
                status = "Vivo",
                origin = Origin("Tierra (C-137)")
            ),
            RickCharacter(
                id = 3,
                name = "Summer Smith",
                image = "https://rickandmortyapi.com/api/character/avatar/3.jpeg",
                species = "Humano",
                status = "Vivo",
                origin = Origin("Tierra (C-137)")
            ),
            RickCharacter(
                id = 4,
                name = "Jerry Smith",
                image = "https://rickandmortyapi.com/api/character/avatar/5.jpeg",
                species = "Humano",
                status = "Vivo",
                origin = Origin("Tierra (C-137)")
            )
        )

        setContent {
            ApprickmortyTheme {
                AppNavigation(characters = sampleCharacters)
            }
        }
    }
}