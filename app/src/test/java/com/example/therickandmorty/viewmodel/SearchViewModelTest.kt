package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.data.remote.dtos.LocationDto
import com.example.therickandmorty.data.remote.dtos.OriginDto
import com.example.therickandmorty.fakeusecase.FakeLoadListUseCaseInt
import com.example.therickandmorty.presentation.viewmodel.SearchViewModel
import com.example.therickandmorty.presentation.viewmodel.actions.SearchAction
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class SearchViewModelTest {

    private lateinit var viewModel: SearchViewModel

    private fun createCharacters(): List<CharacterDto> =
        listOf(
            CharacterDto(
                id = 1,
                name = "Rick",
                status = "alive",
                species = "human",
                type = "",
                gender = "male",
                origin = OriginDto("Earth", ""),
                location = LocationDto("Earth", ""),
                image = "",
                episode = emptyList(),
                url = "",
                created = ""
            ),
            CharacterDto(
                id = 2,
                name = "Morty",
                status = "alive",
                species = "human",
                type = "",
                gender = "male",
                origin = OriginDto("Earth", ""),
                location = LocationDto("Earth", ""),
                image = "",
                episode = emptyList(),
                url = "",
                created = ""
            )
        )

    @Before
    fun setup() {
        val fakeUseCase = FakeLoadListUseCaseInt(createCharacters())
        viewModel = SearchViewModel(fakeUseCase)
    }

    @Test
    fun `applyFilters should update name and status`() {
        viewModel.onAction(
            SearchAction.ApplyFilters(
                name = "Rick",
                status = "alive"
            )
        )

        val state = viewModel.state.value

        assertEquals("Rick", state.name)
        assertEquals("alive", state.status)
    }

    @Test
    fun `applyFilters should expose characters from use case`() = runTest {
        viewModel.onAction(
            SearchAction.ApplyFilters(
                name = "Rick",
                status = "alive"
            )
        )
        val charactersFlow = viewModel.state.value.characters

        assertNotNull(charactersFlow)
    }

}

