package com.danidev.apprickmorty.ui.viewModel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.data.repository.CharacterRepository
import kotlinx.coroutines.launch

class CharacterViewModel(
    private val repository: CharacterRepository = CharacterRepository()
) : ViewModel() {

    var allCharacters by mutableStateOf<List<RickCharacter>>(emptyList())
        private set

    var filteredCharacters by mutableStateOf<List<RickCharacter>>(emptyList())
        private set

    var isLoading by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    var searchQuery by mutableStateOf("")
        private set

    var availablePacks by mutableIntStateOf(3)
        private set

    var openedCharacters by mutableStateOf<List<RickCharacter>>(emptyList())
        private set

    fun initCharacters(fallback: List<RickCharacter>) {
        if (allCharacters.isEmpty()) {
            allCharacters = fallback
            filteredCharacters = fallback
            loadFromApi(fallback)
        }
    }

    fun loadFromApi(fallback: List<RickCharacter> = allCharacters) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            val result = repository.getCharacters()
            result.onSuccess { list ->
                if (list.isNotEmpty()) {
                    allCharacters = list
                    applyFilter(searchQuery)
                }
            }.onFailure { e ->
                errorMessage = e.localizedMessage
                if (allCharacters.isEmpty()) {
                    allCharacters = fallback
                    applyFilter(searchQuery)
                }
            }
            isLoading = false
        }
    }

    fun onSearchQueryChanged(query: String) {
        searchQuery = query
        applyFilter(query)
    }

    private fun applyFilter(query: String) {
        filteredCharacters = if (query.isBlank()) {
            allCharacters
        } else {
            allCharacters.filter {
                it.name.contains(query, ignoreCase = true) ||
                    it.species.contains(query, ignoreCase = true) ||
                    it.status.contains(query, ignoreCase = true)
            }
        }
    }

    fun openPack(): RickCharacter? {
        val pool = if (allCharacters.isNotEmpty()) allCharacters else filteredCharacters
        if (pool.isEmpty()) return null
        val chosen = pool.random()
        openedCharacters = openedCharacters + chosen
        if (availablePacks > 0) {
            availablePacks -= 1
        }
        return chosen
    }

    fun addMorePacks(count: Int = 3) {
        availablePacks += count
    }
}
