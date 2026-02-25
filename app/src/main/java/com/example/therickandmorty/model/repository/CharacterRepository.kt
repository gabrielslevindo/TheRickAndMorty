package com.example.therickandmorty.model.repository

import com.example.therickandmorty.core.data.safeApiCall
import com.example.therickandmorty.core.domain.DataError
import com.example.therickandmorty.core.domain.DataException
import com.example.therickandmorty.core.domain.Result
import com.example.therickandmorty.data.local.CharacterDao
import com.example.therickandmorty.data.remote.CharacterApi
import com.example.therickandmorty.data.remote.dtos.CharacterResponseDto
import com.example.therickandmorty.model.dataclass.CharacterData
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
    override suspend fun getCharacters(page: Int): CharacterResponseDto =
        when (
            val result =
                safeApiCall {
                    characterApi.getCharacters(page)
                }
        ) {
            is Result.Success -> {
                result.data
            }

            is Result.Error -> throw DataException(result.error)
        }

    override suspend fun getFilteredCharacters(
        page: Int?,
        name: String?,
        status: String?,
    ): CharacterResponseDto =
        when (
            val result =
                safeApiCall {
                    characterApi.getFilteredCharacters(page, name, status)
                }
        ) {
            is Result.Success -> result.data

            is Result.Error -> throw DataException(result.error)
        }

    override suspend fun insertFavorite(character: CharacterData) {
        try {
            characterDao.insertCharacter(character)
        } catch (e: Exception) {
            throw DataException(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun deleteFavorite(characterId: Int) {
        try {
            characterDao.deleteCharacterById(characterId)
        } catch (e: Exception) {
            throw DataException(DataError.Local.UNKNOWN)
        }
    }

    override suspend fun isFavorite(characterId: Int): Boolean =
        try {
            characterDao.getCharacterById(characterId) != null
        } catch (e: Exception) {
            throw DataException(DataError.Local.UNKNOWN)
        }

    override suspend fun getAllFavorites(): Flow<List<CharacterData>> =
        try {
            characterDao.getAllFavorites()
        } catch (e: Exception) {
            throw DataException(DataError.Local.UNKNOWN)
        }
}
