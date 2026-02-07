package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.domain.dataclass.CharacterData
import com.example.therickandmorty.fakerepository.FakeCharacterRepository
import com.example.therickandmorty.presentation.viewmodel.FavoritesViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.FavoritesAction
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
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

            dispatcher.scheduler.advanceUntilIdle()

            val finalState =
                viewModel.state
                    .map { it.state }
                    .first { !it.isLoading }

            assertEquals(2, finalState.successApiList.size)
            assertEquals("Character 1", finalState.successApiList[0].name)
        }

    @Test
    fun `toggleFavorite should insert favorite when not already favorite`() =
        runTest {
            val character = createCharacterDataList()[0]

            viewModel.onAction(FavoritesAction.ToggleFavorite(character))

            val finalState =
                viewModel.state
                    .map { it.state }
                    .first { !it.isLoading }

            assertEquals(1, finalState.successApiList.size)
            assertEquals(character.id, finalState.successApiList.first().id)
        }

    @Test
    fun `toggleFavorite should delete favorite when already favorite`() =
        runTest {
            val character = createCharacterDataList()[0]
            repository.insertFavorite(character)

            viewModel.onAction(
                FavoritesAction.ToggleFavorite(character),
            )

            dispatcher.scheduler.advanceUntilIdle()

            val stateView = viewModel.state.value.state

            assertEquals(0, stateView.successApiList.size)
        }

    @Test
    fun `checkIsFavorite should update state to true when character is favorite`() =
        runTest {
            val character = createCharacterDataList()[0]
            repository.insertFavorite(character)

            viewModel.onAction(
                FavoritesAction.IsFavorite(character.id),
            )

            val isFavorite =
                viewModel.state
                    .map { it.isFavorite }
                    .first { it }

            assertEquals(true, isFavorite)
        }

    @Test
    fun `checkIsFavorite should update state to false when character is not favorite`() =
        runTest {
            viewModel.onAction(
                FavoritesAction.IsFavorite(999),
            )

            dispatcher.scheduler.advanceUntilIdle()

            assertEquals(false, viewModel.state.value.isFavorite)
        }
}
