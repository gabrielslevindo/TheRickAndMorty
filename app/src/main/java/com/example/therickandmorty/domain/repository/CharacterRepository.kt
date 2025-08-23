package com.example.therickandmorty.domain.repository

import com.example.therickandmorty.data.local.CharacterDao
import com.example.therickandmorty.data.remote.CharacterApi
import com.example.therickandmorty.data.remote.dtos.CharacterResponseDto
import com.example.therickandmorty.domain.dataclass.CharacterData
import kotlinx.coroutines.flow.Flow

interface CharacterRepository {
    suspend fun getCharacters(page: Int): CharacterResponseDto

    suspend fun getFilteredCharacters(
        page: Int? = null,
        name: String?,
        status: String?,
    ): CharacterResponseDto

    suspend fun insertFavorite(character: CharacterData)

    suspend fun deleteFavorite(characterId: Int)

    suspend fun isFavorite(characterId: Int): Boolean

    suspend fun getAllFavorites(): Flow<List<CharacterData>>
}

class CharacterRepositoryImpl(
    private val characterApi: CharacterApi,
    private val characterDao: CharacterDao,
) : CharacterRepository {
    override suspend fun getCharacters(page: Int): CharacterResponseDto = characterApi.getCharacters(page)

    override suspend fun getFilteredCharacters(
        page: Int?,
        name: String?,
        status: String?,
    ): CharacterResponseDto = characterApi.getFilteredCharacters(page, name, status)

    override suspend fun insertFavorite(character: CharacterData) {
        characterDao.insertCharacter(character)
    }

    override suspend fun deleteFavorite(characterId: Int) {
        characterDao.deleteCharacterById(characterId)
    }

    override suspend fun isFavorite(characterId: Int): Boolean = characterDao.getCharacterById(characterId) != null

    override suspend fun getAllFavorites(): Flow<List<CharacterData>> = characterDao.getAllFavorites()
}
