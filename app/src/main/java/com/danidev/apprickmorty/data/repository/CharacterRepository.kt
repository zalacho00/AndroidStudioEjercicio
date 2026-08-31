package com.danidev.apprickmorty.data.repository

import com.danidev.apprickmorty.data.model.RickCharacter
import com.danidev.apprickmorty.data.remote.RetrofitClient

class CharacterRepository {

    private val api = RetrofitClient.api

    suspend fun getCharacters(): Result<List<RickCharacter>>{
        return  try {
            val response = api.getCharacters(name: String? = null):
            Result.success(response.results)
        } catch (e: Exception){
            Result.failure(e)
        }
    }
}
