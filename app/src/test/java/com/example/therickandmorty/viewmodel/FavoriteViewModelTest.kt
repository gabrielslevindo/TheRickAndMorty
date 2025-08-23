package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.data.local.extensions.toDto
import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.domain.dataclass.CharacterData
import com.example.therickandmorty.fakerepository.FakeCharacterRepository
import com.example.therickandmorty.presentation.states.StateView
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class FavoritesViewModelTest {
    private val dispatcher = StandardTestDispatcher()
    private lateinit var repository: FakeCharacterRepository
    private lateinit var viewModel: FavoritesViewModel

    private fun createCharacterDataList(): List<CharacterData> =
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

    private fun createCharacterDtoList(): List<CharacterDto> = createCharacterDataList().map { it.toDto() }

    @Before
    fun setup() {
        Dispatchers.setMain(dispatcher)
        repository = FakeCharacterRepository()
        viewModel = FavoritesViewModel(repository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `loadFavorites should update state with favorites`() =
        runTest {
            val favorites = createCharacterDataList()
            favorites.forEach { repository.insertFavorite(it) }

            viewModel.loadFavorites()

            // Avança o dispatcher até que todas as coroutines terminem
            dispatcher.scheduler.advanceUntilIdle()

            val expectedState =
                StateView(
                    SuccessApiList = createCharacterDtoList(),
                    isLoading = false,
                    isError = null,
                )
            assertEquals(expectedState, viewModel.state.value)
        }

    @Test
    fun `toggleFavorite should insert favorite when not already favorite`() =
        runTest {
            val character = createCharacterDataList()[0]

            viewModel.toggleFavorite(character)

            // Avança o dispatcher até que todas as coroutines lançadas terminem
            dispatcher.scheduler.advanceUntilIdle()

            // Agora podemos verificar
            assertEquals(1, viewModel.state.value.SuccessApiList.size)
            assertEquals(
                character.id,
                viewModel.state.value.SuccessApiList[0]
                    .id,
            )
        }

    @Test
    fun `toggleFavorite should delete favorite when already favorite`() =
        runTest {
            val character = createCharacterDataList()[0]
            repository.insertFavorite(character)

            viewModel.toggleFavorite(character)

            // Lista de favoritos deve estar vazia
            viewModel.loadFavorites()
            assertEquals(0, viewModel.state.value.SuccessApiList.size)
        }

    @Test
    fun `isFavorite should return true when character is favorite`() =
        runTest {
            val character = createCharacterDataList()[0]
            repository.insertFavorite(character)

            val result = viewModel.isFavorite(character.id)
            assertEquals(true, result)
        }

    @Test
    fun `isFavorite should return false when character is not favorite`() =
        runTest {
            val result = viewModel.isFavorite(999)
            assertEquals(false, result)
        }
}
