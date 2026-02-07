package com.example.therickandmorty.testrepository

import com.example.therickandmorty.data.local.CharacterDao
import com.example.therickandmorty.data.remote.CharacterApi
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.data.remote.dtos.CharacterResponseDto
import com.example.therickandmorty.data.remote.dtos.InfoDto
import com.example.therickandmorty.domain.dataclass.CharacterData
import com.example.therickandmorty.domain.repository.CharacterRepositoryImpl
import io.mockk.MockKAnnotations
import io.mockk.Runs
import io.mockk.clearAllMocks
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.every
import io.mockk.just
import io.mockk.mockk
import io.mockk.verify
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterRepositoryTest {
    private lateinit var characterApi: CharacterApi
    private lateinit var characterDao: CharacterDao
    private lateinit var repository: CharacterRepositoryImpl

    private val dispatcher = StandardTestDispatcher()

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        MockKAnnotations.init(this, relaxed = true)

        characterApi = mockk()
        characterDao = mockk()
        repository = CharacterRepositoryImpl(characterApi, characterDao)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
        clearAllMocks()
    }

    private fun createMockCharacterResponse(): CharacterResponseDto {
        val character1 =
            CharacterDto(
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
            )
        val character2 =
            CharacterDto(
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
            )
        return CharacterResponseDto(
            info = InfoDto(count = 2, pages = 1, next = null, prev = null),
            results = listOf(character1, character2),
        )
    }

    @Test
    fun `getCharacters should return character response from API`() {
        runBlocking {
            val expectedResponse = createMockCharacterResponse()
            coEvery { characterApi.getCharacters(any()) } returns expectedResponse

            val actualResponse = repository.getCharacters(1)

            assertEquals(expectedResponse, actualResponse)
            coVerify { characterApi.getCharacters(1) }
        }
    }

    @Test
    fun `getFilteredCharacters should return filtered character response from API`() {
        runBlocking {
            val expectedResponse = createMockCharacterResponse()
            coEvery {
                characterApi.getFilteredCharacters(
                    any(),
                    any(),
                    any(),
                )
            } returns expectedResponse

            val actualResponse = repository.getFilteredCharacters(1, "name", "alive")

            assertEquals(expectedResponse, actualResponse)
            coVerify { characterApi.getFilteredCharacters(1, "name", "alive") }
        }
    }

    @Test
    fun `insertFavorite should call DAO insertCharacter`() {
        runBlocking {
            val character =
                CharacterData(1, "Character 1", "alive", "human", "male", "image1.jpg", type = "")
            coEvery { characterDao.insertCharacter(character) } just Runs

            repository.insertFavorite(character)

            coVerify { characterDao.insertCharacter(character) }
        }
    }

    @Test
    fun `deleteFavorite should call DAO deleteCharacterById`() {
        runBlocking {
            coEvery { characterDao.deleteCharacterById(1) } just Runs

            repository.deleteFavorite(1)

            coVerify { characterDao.deleteCharacterById(1) }
        }
    }

    @Test
    fun `isFavorite should return true if character exists in DAO`() {
        runBlocking {
            coEvery { characterDao.getCharacterById(1) } returns
                CharacterData(
                    id = 1,
                    name = "Character 1",
                    status = "alive",
                    species = "human",
                    gender = "male",
                    image = "",
                    type = "",
                )

            val result = repository.isFavorite(1)

            assertEquals(true, result)
            coVerify { characterDao.getCharacterById(1) }
        }
    }

    @Test
    fun `getAllFavorites should return flow from DAO`() {
        runBlocking {
            val favorites =
                listOf(
                    CharacterData(
                        id = 1,
                        name = "Character 1",
                        status = "alive",
                        species = "human",
                        gender = "male",
                        image = "image1.jpg",
                        type = "",
                    ),
                    CharacterData(
                        id = 2,
                        name = "Character 2",
                        status = "dead",
                        species = "alien",
                        gender = "female",
                        image = "image2.jpg",
                        type = "",
                    ),
                )
            every { characterDao.getAllFavorites() } returns flowOf(favorites)

            val result = repository.getAllFavorites()

            val emitted = mutableListOf<List<CharacterData>>()
            result.collect { emitted.add(it) }

            assertEquals(favorites, emitted.first())
            verify { characterDao.getAllFavorites() }
        }
    }
}
