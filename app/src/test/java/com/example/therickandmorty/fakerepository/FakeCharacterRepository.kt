package com.example.therickandmorty.fakerepository

import com.example.therickandmorty.data.remote.dtos.CharacterResponseDto
import com.example.therickandmorty.domain.dataclass.CharacterData
import com.example.therickandmorty.domain.repository.CharacterRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeCharacterRepository : CharacterRepository {
    var shouldThrowOnGetAllFavorites = false
    var getAllFavoritesException: RuntimeException? = null

    private val favorites = mutableListOf<CharacterData>()

    override suspend fun getCharacters(page: Int): CharacterResponseDto =
        CharacterResponseDto(
            info =
                com.example.therickandmorty.data.remote.dtos.InfoDto(
                    count = 2,
                    pages = 1,
                    next = null,
                    prev = null,
                ),
            results =
                listOf(
                    com.example.therickandmorty.data.remote.dtos.CharacterDto(
                        id = 1,
                        name = "Character 1",
                        status = "alive",
                        species = "human",
                        type = "_",
                        gender = "male",
                        origin =
                            com.example.therickandmorty.data.remote.dtos
                                .OriginDto("Earth", "url"),
                        location =
                            com.example.therickandmorty.data.remote.dtos
                                .LocationDto("Earth", "url"),
                        image = "image1.jpg",
                        episode = listOf("episode1", "episode2"),
                        url = "url",
                        created = "created",
                    ),
                    com.example.therickandmorty.data.remote.dtos.CharacterDto(
                        id = 2,
                        name = "Character 2",
                        status = "dead",
                        species = "alien",
                        type = "_",
                        gender = "female",
                        origin =
                            com.example.therickandmorty.data.remote.dtos
                                .OriginDto("Mars", "url"),
                        location =
                            com.example.therickandmorty.data.remote.dtos
                                .LocationDto("Mars", "url"),
                        image = "image2.jpg",
                        episode = listOf("episode1", "episode2"),
                        url = "url",
                        created = "created",
                    ),
                ),
        )

    override suspend fun getFilteredCharacters(
        page: Int?,
        name: String?,
        status: String?,
    ): CharacterResponseDto =
        CharacterResponseDto(
            info =
                com.example.therickandmorty.data.remote.dtos.InfoDto(
                    count = 1,
                    pages = 1,
                    next = null,
                    prev = null,
                ),
            results =
                listOf(
                    com.example.therickandmorty.data.remote.dtos.CharacterDto(
                        id = 3,
                        name = "Filtered Character",
                        status = status ?: "unknown",
                        species = "human",
                        type = "_",
                        gender = "male",
                        origin =
                            com.example.therickandmorty.data.remote.dtos
                                .OriginDto("Earth", "url"),
                        location =
                            com.example.therickandmorty.data.remote.dtos
                                .LocationDto("Earth", "url"),
                        image = "filtered_image.jpg",
                        episode = listOf("episode1"),
                        url = "url",
                        created = "created",
                    ),
                ),
        )

    override suspend fun insertFavorite(character: CharacterData) {
        favorites.add(character)
    }

    override suspend fun deleteFavorite(characterId: Int) {
        favorites.removeAll { it.id == characterId }
    }

    override suspend fun isFavorite(characterId: Int): Boolean = favorites.any { it.id == characterId }

    override suspend fun getAllFavorites(): Flow<List<CharacterData>> = flowOf(favorites)
}
