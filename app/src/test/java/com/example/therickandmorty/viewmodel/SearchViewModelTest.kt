package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.data.local.extensions.toDto
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
class SearchViewModelTest {
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
                type = "",
                gender = "male",
                image = "image1.jpg",
            ),
            CharacterData(
                id = 2,
                name = "Character 2",
                status = "dead",
                species = "alien",
                type = "",
                gender = "female",
                image = "image2.jpg",
            ),
        )

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
            dispatcher.scheduler.advanceUntilIdle() // espera todas as coroutines terminarem

            val expectedState =
                StateView(
                    SuccessApiList = favorites.map { it.toDto() }, // <- aqui está a correção
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

            dispatcher.scheduler.advanceUntilIdle()

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

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(0, viewModel.state.value.SuccessApiList.size)
        }
}
