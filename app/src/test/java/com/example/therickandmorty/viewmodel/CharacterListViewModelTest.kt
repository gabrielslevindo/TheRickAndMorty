package com.example.therickandmorty.viewmodel

import com.example.therickandmorty.dispatcher.MainDispatcherRule
import com.example.therickandmorty.fakeusecase.FakeLoadListUseCaseCharacter
import com.example.therickandmorty.presentation.viewmodel.CharacterListViewModel
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertNotSame
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class CharacterListViewModelTest {
    @get:Rule
    val dispatcherRule = MainDispatcherRule()

    private lateinit var fakeUseCase: FakeLoadListUseCaseCharacter
    private lateinit var viewModel: CharacterListViewModel

    @Before
    fun setup() {
        fakeUseCase = FakeLoadListUseCaseCharacter()
        viewModel = CharacterListViewModel(fakeUseCase)
    }

    @Test
    fun `use case is not called before state is collected`() {
        runTest {
            assertEquals(0, fakeUseCase.callCount)
        }
    }

    @Test
    fun `use case is called when state is collected`() {
        runTest {
            val job =
                launch {
                    viewModel.state.collect { }
                }

            advanceUntilIdle()

            assertEquals(1, fakeUseCase.callCount)
            assertNull(fakeUseCase.receivedName)
            assertNull(fakeUseCase.receivedStatus)

            job.cancel()
        }
    }

    @Test
    fun `state characters is updated after loadCharacters`() {
        runTest {
            val initialCharacters = viewModel.state.value.characters

            val job =
                launch {
                    viewModel.state.collect { }
                }

            advanceUntilIdle()

            val updatedCharacters = viewModel.state.value.characters

            assertNotSame(initialCharacters, updatedCharacters)

            job.cancel()
        }
    }

    @Test
    fun `use case is not called multiple times while subscribed`() {
        runTest {
            val job =
                launch {
                    viewModel.state.collect { }
                }

            advanceUntilIdle()

            assertEquals(1, fakeUseCase.callCount)

            advanceUntilIdle()

            assertEquals(1, fakeUseCase.callCount)

            job.cancel()
        }
    }
}
