package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.data.remote.dtos.CharacterDto
import com.example.therickandmorty.data.remote.dtos.LocationDto
import com.example.therickandmorty.data.remote.dtos.OriginDto
import com.example.therickandmorty.fakeusecase.FakeLoadListUseCaseInt
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Test
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {
    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup() {
        val fakeCharacters = createMockCharacters()
        val fakeUseCase = FakeLoadListUseCaseInt(fakeCharacters)

        viewModel = CharacterListViewModel(loadListUseCaseInt = fakeUseCase)
    }

    @Test
    fun `loadCharacters should expose flow from use case`() =
        runTest {
            val charactersFlow = viewModel.state.value.characters

            assertNotNull(charactersFlow)
        }
}

private fun createMockCharacters(): List<CharacterDto> =
    listOf(
        CharacterDto(
            id = 1,
            name = "Character 1",
            status = "alive",
            species = "human",
            type = "",
            gender = "male",
            origin = OriginDto("Earth", ""),
            location = LocationDto("Earth", ""),
            image = "image1.jpg",
            episode = emptyList(),
            url = "",
            created = "",
        ),
        CharacterDto(
            id = 2,
            name = "Character 2",
            status = "dead",
            species = "alien",
            type = "",
            gender = "female",
            origin = OriginDto("Mars", ""),
            location = LocationDto("Mars", ""),
            image = "image2.jpg",
            episode = emptyList(),
            url = "",
            created = "",
        ),
    )
